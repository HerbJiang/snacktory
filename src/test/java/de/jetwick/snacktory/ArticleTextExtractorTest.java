package de.jetwick.snacktory;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class ArticleTextExtractorTest {

    ArticleTextExtractor extractor;
    Converter c;

    @Before
    public void setup() throws Exception {
        c = new Converter();
        extractor = new ArticleTextExtractor();
    }

    @Test
    public void testData1() throws Exception {
        // ? http://www.npr.org/blogs/money/2010/10/04/130329523/how-fake-money-saved-brazil
        JResult res = extractor.extractContent(readFileAsString("test_data/1.html"));
        assertEquals("How Fake Money Saved Brazil : Planet Money : NPR", res.getTitle());
        assertTrue(res.getText().startsWith("This is a story about how an economist and his buddies tricked the people of Brazil into saving the country from rampant inflation. They had a crazy, unlikely plan, and it worked."));
        assertTrue(res.getText(), res.getText().endsWith("\"How Four Drinking Buddies Saved Brazil.\""));
        assertEquals("http://media.npr.org/assets/img/2010/10/04/real_wide.jpg?t=1286218782&s=3", res.getImageUrl());
        assertTrue(res.getKeywords().isEmpty());
    }

    @Test
    public void testData2() throws Exception {
        // http://benjaminste.in/post/1223476561/hey-guys-whatcha-doing
        JResult res = extractor.extractContent(readFileAsString("test_data/2.html"));
        assertEquals("Hey guys, whatcha doing?", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("This month is the 15th anniversary of my last CD."));
        assertTrue(res.getKeywords().isEmpty());
    }

    @Test
    public void testData3() throws Exception {
        JResult res = extractor.extractContent(readFileAsString("test_data/3.html"));
        assertTrue("data3:" + res.getText(), res.getText().startsWith("October 2010 Silicon Valley proper is mostly suburban sprawl. At first glance it "));
        assertTrue(res.getText().endsWith(" and Jessica Livingston for reading drafts of this."));
        assertTrue(res.getKeywords().isEmpty());
    }

    @Test
    public void testData5() throws Exception {
        JResult res = extractor.extractContent(readFileAsString("test_data/5.html"));
        assertTrue("data5:" + res.getText(), res.getText().startsWith("Hackers unite in Stanford"));
//        assertTrue(res.getText().endsWith("have beats and bevvies a-plenty. RSVP here.    "));
        assertTrue(res.getKeywords().isEmpty());
    }

    @Test
    public void testCNN() throws Exception {
        // http://edition.cnn.com/2011/WORLD/africa/04/06/libya.war/index.html?on.cnn=1
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("cnn.html")));
        assertEquals("Gadhafi asks Obama to end NATO bombing", res.getTitle());
        assertEquals("/2011/WORLD/africa/04/06/libya.war/t1larg.libyarebel.gi.jpg", res.getImageUrl());
        assertTrue("cnn:" + res.getText(), res.getText().startsWith("Tripoli, Libya (CNN) -- As rebel and pro-government forces in Libya maneuvered on the battlefield Wedn"));
    }

    @Test
    public void testWordpress() throws Exception {
        // http://karussell.wordpress.com/
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("wordpress.html")));
//        System.out.println("wordpress:" + res.getText());
        assertEquals("Twitter API and Me « Find Time for the Karussell", res.getTitle());
        assertTrue("wordpress:" + res.getText(), res.getText().startsWith("I have a love hate relationship with Twitter. As a user I see "));
    }

    @Test
    public void testFirefox() throws Exception {
        // http://www.golem.de/1104/82797.html
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("golem.html")));
//        System.out.println("firefox:" + res.getText());
//        assertTrue(res.getText(), res.getText().startsWith("Unter dem Namen \"Aurora\" hat Firefox einen"));
        assertTrue(res.getText(), res.getText().startsWith("Mozilla hat Firefox 5.0a2 veröffentlicht und zugleich eine erste Entwicklerversion von Firefox 6 freigegeben."));
//        assertEquals("http://scr3.golem.de/screenshots/1104/Firefox-Aurora/thumb480/aurora-nighly-beta-logos.png", res.getImageUrl());
//        assertEquals("http://www.golem.de/1104/82797-9183-i.png", res.getImageUrl());
        assertEquals("Mozilla: Vorabversionen von Firefox 5 und 6 veröffentlicht", res.getTitle());
    }

    @Test
    public void testYomiuri() throws Exception {
        // http://www.yomiuri.co.jp/e-japan/gifu/news/20110410-OYT8T00124.htm
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("yomiuri.html")));
        assertEquals("色とりどりのチューリップ : 岐阜 : 地域 : YOMIURI ONLINE（読売新聞）", res.getTitle());
        assertTrue(res.getText().startsWith("海津市海津町の国営木曽三川公園で、チューリップが見頃を迎えている。２０日までは「チューリップ祭」が開かれており、大勢の人たちが多彩な色や形を鑑賞している＝写真＝"));
        assertEquals(Arrays.asList("読売新聞", "地域"), res.getKeywords());
    }

    @Test
    public void testFAZ() throws Exception {
        // http://www.faz.net/s/Rub469C43057F8C437CACC2DE9ED41B7950/Doc~EBA775DE7201E46E0B0C5AD9619BD56E9~ATpl~Ecommon~Scontent.html
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("faz.html")));
//        assertTrue(res.getText(), res.getText().startsWith("Im Gespräch: Umweltaktivist Stewart Brand"));
        assertTrue(res.getText(), res.getText().startsWith("Deutschland hat vor, ganz auf Atomkraft zu verzichten. Ist das eine gute"));
        assertEquals("/m/{5F104CCF-3B5A-4B4C-B83E-4774ECB29889}g225_4.jpg", res.getImageUrl());

        assertEquals(Arrays.asList("Atomkraft", "Deutschland", "Jahren", "Atommüll", "Fukushima", "Problem", "Brand", "Kohle", "2011", "11", 
                                   "Stewart", "Atomdebatte", "Jahre", "Boden", "Treibhausgase", "April", "Welt", "Müll", "Radioaktivität", 
                                   "Gesamtbild", "Klimawandel", "Reaktoren", "Verzicht", "Scheinheiligkeit", "Leute", "Risiken", "Löcher", 
                                   "Fusion", "Gefahren", "Land"),
                   res.getKeywords());
    }

    @Test
    public void testRian() throws Exception {
        // http://en.rian.ru/world/20110410/163458489.html
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("rian.html")));
        assertTrue(res.getText(), res.getText().startsWith("About 15,000 people took to the streets in Tokyo on Sunday to protest against th"));
        assertEquals("Japanese rally against nuclear power industry", res.getTitle());
        assertEquals("/favicon.ico", res.getFaviconUrl());
        assertTrue(res.getKeywords().isEmpty());
    }

    @Test
    public void testJetwick() throws Exception {
        // http://jetwick.com
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("jetwick.html")));
//        assertTrue(res.getText(), res.getText().startsWith("Search twitter without noise"));
//        assertEquals("img/yourkit.png", res.getImageUrl());
        assertEquals(Arrays.asList("news", "twitter", "search", "jetwick"), res.getKeywords());
    }

    @Test
    public void testVimeo() throws Exception {
        // http://vimeo.com/20910443
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("vimeo.html")));
        assertTrue(res.getText(), res.getText().startsWith("1 month ago 1 month ago: Fri, Mar 11, 2011 2:24am EST (Eastern Standard Time) See all Show me 1. finn. & Dirk von Lowtzow"));
        assertTrue(res.getTitle(), res.getTitle().startsWith("finn. & Dirk von Lowtzow \"CRYING IN THE RAIN\""));
//        assertEquals("http://b.vimeocdn.com/ts/134/104/134104048_200.jpg", res.getImageUrl());
        assertEquals("", res.getVideoUrl());
        assertEquals(Arrays.asList("finn", "finn.", "Dirk von Lowtzow", "crying in the rain", "I wish I was someone else", "Tocotronic", 
                                   "Sunday Service", "Indigo", "Patrick Zimmer", "Patrick Zimmer aka finn.", "video", "video sharing", 
                                   "digital cameras", "videoblog", "vidblog", "video blogging", "home video", "home movie"),
                     res.getKeywords());
    }

    @Test
    public void testYoutube() throws Exception {
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("youtube.html")));
//        assertTrue(res.getText(), res.getText().startsWith("The makers of doom used remixed version of real metal songs for many"));
        assertTrue(res.getText(), res.getText().startsWith("Master of the Puppets by Metallica. Converted to 8 bit with GSXCC. Original verson can be found us"));

        assertEquals("Master of the Puppets 8-bit", res.getTitle());
        assertEquals("http://i4.ytimg.com/vi/wlupmjrfaB4/default.jpg", res.getImageUrl());
        assertEquals("http://www.youtube.com/v/wlupmjrfaB4?version=3", res.getVideoUrl());
    }

    @Test
    public void testSpiegel() throws Exception {
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("spiegel.html")));
        assertTrue(res.getText(), res.getText().startsWith("Da ist er wieder, der C64: Eigentlich längst ein Relikt der Technikgeschichte, soll der "));
    }

    @Test
    public void testGithub() throws Exception {
        // https://github.com/ifesdjeen/jReadability
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("github.html")));
//        System.out.println("github:" + res.getText());
//        assertTrue(res.getText().isEmpty());
        assertTrue(res.getDescription(), res.getDescription().startsWith("Article text extractor from given HTML text"));

        // this would be awsome:
        assertTrue(res.getText(), res.getText().startsWith("= jReadability This is a small helper utility (only 130 lines of code) for pepole"));
        // this would be not good:
//        assertTrue(res.getText(), res.getText().startsWith("ifesdjeen / jReadability Admin Watch Unwatch Fork Where do you want to for"));
    }

    @Test
    public void testITunes() throws Exception {
        // http://itunes.apple.com/us/album/21/id420075073
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("itunes.html")));
        assertTrue(res.getText(), res.getText().startsWith("What else can be said of this album other than that it is simply amazing? Adele's voice is powerful, vulnerable, assured, and heartbreaking all in one fell swoop."));
        assertTrue("itunes:" + res.getDescription(), res.getDescription().startsWith("Preview songs from 21 by ADELE"));
    }

    @Test
    public void testTwitpic() throws Exception {
        // http://twitpic.com/4k1ku3
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("twitpic.html")));
        assertEquals("It’s hard to be a dinosaur. on Twitpic", res.getTitle());
//        assertEquals("", res.getText());
//        assertTrue(res.getText(), res.getText().isEmpty());
    }

    @Test
    public void testTwitpic2() throws Exception {
        // http://twitpic.com/4kuem8
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("twitpic2.html")));
        assertEquals("*Not* what you want to see on the fetal monitor when your wif... on Twitpic", res.getTitle());
//        assertEquals("", res.getText());
    }

    @Test
    public void testHeise() throws Exception {
        // http://www.heise.de/newsticker/meldung/Internet-Explorer-9-jetzt-mit-schnellster-JavaScript-Engine-1138062.html
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("heise.html")));
        assertEquals("", res.getImageUrl());
        assertEquals("Internet Explorer 9 jetzt mit schnellster JavaScript-Engine", res.getTitle());
        assertTrue(res.getText().startsWith("Microsoft hat heute eine siebte Platform Preview des Internet Explorer veröffentlicht. In den nur dr"));
    }

    @Test
    public void testTechcrunch() throws Exception {
        // http://techcrunch.com/2011/04/04/twitter-advanced-search/
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("techcrunch.html")));
//        System.out.println("techcrunch:" + res.getTitle());        
        assertEquals("http://tctechcrunch.files.wordpress.com/2011/04/screen-shot-2011-04-04-at-12-11-36-pm.png?w=285&h=85", res.getImageUrl());
        assertEquals("Twitter Finally Brings Advanced Search Out Of Purgatory; Updates Discovery Algorithms", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("A couple weeks ago, we wrote a post wishing Twitter a happy fifth birthday, but also noting "));
    }

    @Test
    public void testEngadget() throws Exception {
        // http://www.engadget.com/2011/04/09/editorial-androids-problem-isnt-fragmentation-its-contamina/
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("engadget.html")));
        assertTrue(res.getText(), res.getText().startsWith("Editorial: Android's problem isn't fragmentation, it's contamination This thought was first given voice by Myriam Joire on last night's Mobile Podcast, and the"));
        assertEquals("http://www.blogcdn.com/www.engadget.com/media/2011/04/11x0409mnbvhg_thumbnail.jpg", res.getImageUrl());
        assertEquals("Editorial: Android's problem isn't fragmentation, it's contamination -- Engadget", res.getTitle());
    }

    @Test
    public void testDiariovasco() throws Exception {
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("diariovasco.com.htm")));
        assertEquals("Más allá de los ayatolás. diariovasco.com", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("No somos árabes, somos persas. No hablamos árabe, hablamos farsi». Las dos reglas de oro para empezar con buen pie cualquier relación en la república islámica, además de cubrirse con un pañuelo en caso de las mujeres y vestirse de forma islámicamente"));
        assertEquals("", res.getImageUrl());
    }


    @Test
    public void testEngadgetChinese() throws Exception {
        // http://chinese.engadget.com/2011/07/19/victorinox-swiss-army-slim-slim-duo/
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("engadget_chinese.html")));
        assertTrue(res.getText(), res.getText().startsWith("相信對經常郊遊的朋友來說，瑞士軍刀 Victorinox 的名字絕對不會陌生，今次要介紹的雖然也是 Victorinox，但卻不是常常見到的小刀和鏍絲起子"));
        assertEquals("http://www.blogcdn.com/chinese.engadget.com/media/2011/07/2011-07-19-victorinox.jpg", res.getImageUrl());
        assertEquals("不怕被搜到的瑞士軍刀 Victorinox Swiss Army Slim, Slim Duo 隨身碟上市", res.getTitle());
    }

    @Test
    public void testUdn() throws Exception {
        // http://udn.com/NEWS/NATIONAL/NATS1/6892153.shtml
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("udn_com_6892153.shtml")));
        assertTrue(res.getText(), res.getText().startsWith("衛生署食品藥物管理局副局長吳秀英則強調，衛生署一定會做好三管五卡，從境外、邊境與市面上進行抽檢，捍衛國人健康"));
        assertEquals("udn_com_6892153_files/6892153-2742383.JPG", res.getImageUrl());
        assertEquals("經部：我方立場比韓國更多堅持", res.getTitle());
    }
    
    @Test
    public void testChinatimes() throws Exception {
        // http://money.chinatimes.com/news/news-content.aspx?id=20120209002560&cid=1210
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("chinatimes.htm")));
        assertTrue(res.getText(), res.getText().startsWith("手握兩百億元現金的「現金王」英業達會長葉國一，近幾個月在市場求售不動產與物業"));
        assertEquals("./chinatimes_files/20120210002579.jpg", res.getImageUrl());
        assertEquals("台灣「現金王」葉國一 房地產抵押設定一六二億｜產業情報｜理財新聞｜中時理財", res.getTitle());
    }
    
    @Test
    public void testChinatimes2() throws Exception {
        // http://money.chinatimes.com/news/news-content.aspx?id=20120209002560&cid=1210
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("chinatimes2.htm")));
        assertTrue(res.getText(), res.getText().startsWith("    據外電報導，美光科技可能繼2008年併購DRAM大廠奇夢達之後，再度在競標案中，取得聲請破產保護的日商爾必達的經營權。"));
        assertEquals("", res.getImageUrl());
        assertEquals("美光取得爾必達經營權 聯台美日抗韓成形 ｜科技新聞｜中時電子報", res.getTitle());
    }
    
    @Test
    public void testNOWNews() throws Exception {
        // http://money.chinatimes.com/news/news-content.aspx?id=20120209002560&cid=1210
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("nownews.htm")));
        assertTrue(res.getText(), res.getText().startsWith("本報訊 中央選舉委員會議今天決議，總統馬英九在第13任總統選舉投票日"));
        assertEquals("http://static.nownews.com/newspic/1943/i1943385.jpg", res.getImageUrl());
        assertEquals("投票日臉書籲支持　馬遭罰鍰", res.getTitle());
    }
    
    @Test
    public void testBNext() throws Exception {
        // http://www.bnext.com.tw/Article/view/cid/103/id/23225
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("bnext.htm")));
        assertTrue(res.getText(), res.getText().startsWith("過去半年，OpenSignalMaps在195個國家收集超過68萬台Android設備的數據，調查結果"));
        assertEquals("http://tctechcrunch2011.files.wordpress.com/2012/05/devices.png", res.getImageUrl());
        assertEquals("Android設備不只百百款，統計顯示共有3,997種機型", res.getTitle());
    }
    
    @Test
    public void testSinaHK() throws Exception {
        // http://news.sina.com.hk/news/1559/3/1/2593481/1.html
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("sina.hk.htm")));
        assertEquals("溫總最後記者會將成焦點", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("【明報專訊】全國政協大會明天開幕，為一年一度的人大、政協「兩會」拉開序幕，人大也將在後天（5日）開幕。今年「兩會」後的例牌總理記者會，將會是溫家寶最後一次記者會，勢將成為傳媒焦點，人大今年將審議的《刑事訴訟法修正案》和十二屆人大代表產生辦法及名額分配，亦引人注目"));
        assertEquals("http://www.sina.com.hk/08/images/sinalogo.gif", res.getImageUrl());
    }
    
    @Test
    public void testLepoint() throws Exception {
        // http://www.lepoint.fr/politique/election-presidentielle-2012/sarkozy-et-hollande-echos-croises-de-campagne-24-02-2012-1434613_324.php
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("lepoint.htm")));
        assertTrue(res.getText(), res.getText().startsWith("Hollande vs Sarkozy, c'est parti. Alors que les sondages éloignent la perspective d'un nouveau 21 avril,"));
        assertEquals("http://www.lepoint2.com/sons/elysee-420.jpg", res.getImageUrl());  // No good
        assertEquals("Hollande et Sarkozy cherchent du travail", res.getTitle());
    }
    
    @Test
    public void testLefigaro() throws Exception {
        // http://www.lepoint.fr/politique/election-presidentielle-2012/sarkozy-et-hollande-echos-croises-de-campagne-24-02-2012-1434613_324.php
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("lefigaro.htm")));
        assertTrue(res.getText(), res.getText().startsWith("L'État n'est pas le seul à bénéficier des largesses de la SNCF. La société"));
        assertEquals("http://www.lefigaro.fr/medias/2012/02/24/97d70314-5ea6-11e1-92d6-7ad6ef851b19-493x328.jpg", res.getImageUrl());  // No good
        assertEquals("Sociétés : La SNCF va verser 400 euros à ses cheminots", res.getTitle());
    }
    
    @Test
    public void testTwitterblog() throws Exception {
        // http://engineering.twitter.com/2011/04/twitter-search-is-now-3x-faster_1656.html
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("twitter.html")));
        assertEquals("Twitter Engineering: Twitter Search is Now 3x Faster", res.getTitle());
        assertEquals("http://4.bp.blogspot.com/-CmXJmr9UAbA/TZy6AsT72fI/AAAAAAAAAAs/aaF5AEzC-e4/s72-c/Blender_Tsunami.jpg", res.getImageUrl());
//        assertEquals("http://4.bp.blogspot.com/-CmXJmr9UAbA/TZy6AsT72fI/AAAAAAAAAAs/aaF5AEzC-e4/s400/Blender_Tsunami.jpg", res.getImageUrl());
        assertTrue("twitter:" + res.getText(), res.getText().startsWith("In the spring of 2010, the search team at Twitter started to rewrite our search engine in order to serve our ever-growin"));
    }

    @Test
    public void testTazBlog() throws Exception {
        // http://www.taz.de/1/politik/asien/artikel/1/anti-atomkraft-nein-danke/
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("taz.html")));
        assertTrue("taz:" + res.getText(), res.getText().startsWith("Absolute Minderheit: Im Shiba-Park in Tokio treffen sich jetzt jeden Sonntag die Atomkraftgegner. Sie blicken neidisch auf die Anti-AKW-Bewegung in Deutschland. "));
        assertEquals("Protestkultur in Japan nach der Katastrophe: Anti-Atomkraft? Nein danke!", res.getTitle());
        assertEquals("/uploads/tx_hptazarticle/../hp_taz_img/full/antiakwprotestjapandapd.20110410-19.jpg", res.getImageUrl());
    }

    @Test
    public void testFacebook() throws Exception {
        // http://www.facebook.com/ejdionne/posts/10150154175658687
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("facebook.html")));
        assertTrue(res.getText(), res.getText().startsWith("In my column tomorrow, I urge President Obama to end the spectacle of"));
        assertEquals("http://profile.ak.fbcdn.net/hprofile-ak-snc4/41782_97057113686_1357_q.jpg", res.getImageUrl());
        assertEquals("In my column...", res.getTitle());
    }

    @Test
    public void testFacebook2() throws Exception {
        // http://www.facebook.com/permalink.php?story_fbid=214289195249322&id=101149616624415 
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("facebook2.html")));
        assertTrue(res.getText(), res.getText().startsWith("Sommer is the best time to wear Jetwick T-Shirts!"));
        assertEquals("", res.getImageUrl());
        assertEquals("Sommer is the best...", res.getTitle());
    }

    @Test
    public void testBlogger() throws Exception {
        // http://blog.talawah.net/2011/04/gavin-king-unviels-red-hats-top-secret.html
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("blogger.html")));
        assertTrue(res.getText(), res.getText().startsWith("Gavin King unveils Red Hat's Java killer"));
//        assertTrue(res.getText(), res.getText().startsWith("Gavin King of Red Hat/Hibernate/Seam fame recently unveiled the top secret project that"));
        assertEquals("http://3.bp.blogspot.com/-cyMzveP3IvQ/TaR7f3qkYmI/AAAAAAAAAIk/mrChE-G0b5c/s200/Java.png", res.getImageUrl());
        assertEquals("The Brain Dump: Gavin King unveils Red Hat's Java killer successor: The Ceylon Project", res.getTitle());
        assertEquals("http://blog.talawah.net/feeds/posts/default?alt=rss", res.getRssUrl());
    }

    @Test
    public void testNyt() throws Exception {
        // http://dealbook.nytimes.com/2011/04/11/for-defense-in-galleon-trial-no-time-to-rest/
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("nyt.html")));
        assertEquals("http://graphics8.nytimes.com/images/2011/04/12/business/dbpix-raj-rajaratnam-1302571800091/dbpix-raj-rajaratnam-1302571800091-tmagSF.jpg",
                res.getImageUrl());
        assertTrue(res.getText(), res.getText().startsWith("I wouldn’t want to be Raj Rajaratnam’s lawyer right now."));
    }

    @Test
    public void testHuffingtonpost() throws Exception {
        // "http://www.huffingtonpost.com/2010/08/13/federal-reserve-pursuing_n_681540.html";        
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("huffingtonpost.html")));
        assertEquals("Federal Reserve's Low Rate Policy Is A 'Dangerous Gamble,' Says Top Central Bank Official", res.getTitle());
        assertTrue(res.getText(), res.getText().startsWith("A top regional Federal Reserve official sharply"));
        assertEquals("http://i.huffpost.com/gen/157611/thumbs/s-FED-large.jpg", res.getImageUrl());
    }

    @Test
    public void testTechcrunch2() throws Exception {
        //String url = "http://techcrunch.com/2010/08/13/gantto-takes-on-microsoft-project-with-web-based-project-management-application/";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("techcrunch2.html")));
        assertEquals("Gantto Takes On Microsoft Project With Web-Based Project Management Application", article.getTitle());
        assertTrue(article.getText(), article.getText().startsWith("Y Combinator-backed Gantto is launching"));
        assertEquals("http://tctechcrunch.files.wordpress.com/2010/08/gantto.jpg", article.getImageUrl());
    }

    @Test
    public void testCnn2() throws Exception {
        //String url = "http://www.cnn.com/2010/POLITICS/08/13/democrats.social.security/index.html";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("cnn2.html")));
        assertEquals("Democrats to use Social Security against GOP this fall", article.getTitle());
        assertTrue(article.getText(), article.getText().startsWith("Washington (CNN) -- Democrats pledged "));
        assertEquals(article.getImageUrl(), "http://i.cdn.turner.com/cnn/2010/POLITICS/08/13/democrats.social.security/story.kaine.gi.jpg");
    }

    @Test
    public void testBusinessweek2() throws Exception {
        //String url = "http://www.businessweek.com/magazine/content/10_34/b4192048613870.htm";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("businessweek2.html")));
        assertTrue(article.getText(), article.getText().startsWith("There's discord on Wall Street: Strategists at major American investment "));
        assertEquals("http://images.businessweek.com/mz/covers/current_120x160.jpg", article.getImageUrl());
    }

    @Test
    public void testFoxnews() throws Exception {
        //String url = "http://www.foxnews.com/politics/2010/08/14/russias-nuclear-help-iran-stirs-questions-improved-relations/";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("foxnews.html")));
        assertTrue("Foxnews:" + article.getText(), article.getText().startsWith("Apr. 8: President Obama signs the New START treaty with Russian President Dmitry Medvedev at the Prague Castle"));
        assertEquals("http://a57.foxnews.com/static/managed/img/Politics/397/224/startsign.jpg", article.getImageUrl());
    }

    @Test
    public void testStackoverflow() throws Exception {
        //String url = "http://stackoverflow.com/questions/3553693/wicket-vs-vaadin/3660938";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("stackoverflow.html")));
//        assertTrue("stackoverflow:" + article.getText(), article.getText().startsWith("Hi, Am torn between wicket and vaadin. i am starting a micro-isv"));
        assertTrue("stackoverflow:" + article.getText(), article.getText().startsWith("I think I've invested some time for both frameworks. I really like bo"));
        assertEquals("wicket vs Vaadin", article.getTitle());
        assertEquals("", article.getImageUrl());
    }

    @Test
    public void testAolnews() throws Exception {
        //String url = "http://www.aolnews.com/nation/article/the-few-the-proud-the-marines-getting-a-makeover/19592478";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("aolnews.html")));
        assertEquals("http://o.aolcdn.com/art/ch_news/aol_favicon.ico", article.getFaviconUrl());
        assertTrue(article.getText(), article.getText().startsWith("WASHINGTON (Aug. 13) -- Declaring \"the maritime soul of the Marine Corps"));
        assertEquals("http://o.aolcdn.com/photo-hub/news_gallery/6/8/680919/1281734929876.JPEG", article.getImageUrl());
        assertEquals(Arrays.asList("news", "update", "breaking", "nation", "U.S.", "elections", "world", "entertainment", "sports", "business", 
                                   "weird news", "health", "science", "latest news articles", "breaking news", "current news", "top news"), 
                     article.getKeywords());
    }

    @Test
    public void testWallstreetjournal() throws Exception {
        //String url = "http://online.wsj.com/article/SB10001424052748704532204575397061414483040.html";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("wsj.html")));
        assertTrue(article.getText(), article.getText().startsWith("The Obama administration has paid out less than a third of the nearly $230 billion"));
        assertEquals("http://si.wsj.net/public/resources/images/OB-JO747_stimul_D_20100814113803.jpg", article.getImageUrl());
    }

    @Test
    public void testUsatoday() throws Exception {
        //String url = "http://content.usatoday.com/communities/thehuddle/post/2010/08/brett-favre-practices-set-to-speak-about-return-to-minnesota-vikings/1";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("usatoday.html")));
        assertTrue(article.getText(), article.getText().startsWith("Brett Favre couldn't get away from the"));
        assertEquals("http://i.usatoday.net/communitymanager/_photos/the-huddle/2010/08/18/favrespeaksx-inset-community.jpg", article.getImageUrl());
    }

    @Test
    public void testUsatoday2() throws Exception {
        //String url = "http://content.usatoday.com/communities/driveon/post/2010/08/gm-finally-files-for-ipo/1";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("usatoday2.html")));
        assertTrue(article.getText(), article.getText().startsWith("General Motors just filed with the Securities and Exchange "));
        assertEquals("http://i.usatoday.net/communitymanager/_photos/drive-on/2010/08/18/cruzex-wide-community.jpg", article.getImageUrl());
    }

    @Test
    public void testEspn() throws Exception {
        //String url = "http://sports.espn.go.com/espn/commentary/news/story?id=5461430";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("espn.html")));
        assertTrue(article.getText(), article.getText().startsWith("If you believe what college football coaches have said about sports"));
        assertEquals("http://a.espncdn.com/photo/2010/0813/ncf_i_mpouncey1_300.jpg", article.getImageUrl());
    }

    @Test
    public void testGizmodo() throws Exception {
        //String url = "http://www.gizmodo.com.au/2010/08/xbox-kinect-gets-its-fight-club/";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("gizmodo.html")));
        assertTrue(article.getText(), article.getText().startsWith("You love to punch your arms through the air"));
        assertEquals("", article.getImageUrl());
    }

    @Test
    public void testEngadget2() throws Exception {
        //String url = "http://www.engadget.com/2010/08/18/verizon-fios-set-top-boxes-getting-a-new-hd-guide-external-stor/";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("engadget2.html")));
        assertTrue(article.getText(), article.getText().startsWith("Verizon FiOS set-top boxes getting a new HD guide"));
//        assertTrue(article.getText(), article.getText().startsWith("Streaming and downloading TV content to mobiles is nice"));
        assertEquals("http://www.blogcdn.com/www.engadget.com/media/2010/08/44ni600_thumbnail.jpg", article.getImageUrl());
    }

    @Test
    public void testWired() throws Exception {
        //String url = "http://www.wired.com/playbook/2010/08/stress-hormones-boxing/";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("wired.html")));
        assertTrue(article.getText(), article.getText().startsWith("On November 25, 1980, professional boxing"));
        assertEquals("Stress Hormones Could Predict Boxing Dominance", article.getTitle());
        assertEquals("http://www.wired.com/playbook/wp-content/uploads/2010/08/fight_f-660x441.jpg", article.getImageUrl());
    }

    @Test
    public void tetGigaohm() throws Exception {
        //String url = "http://gigaom.com/apple/apples-next-macbook-an-800-mac-for-the-masses/";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("gigaom.html")));
        assertTrue(article.getText(), article.getText().startsWith("The MacBook Air is a bold move forward "));
        assertEquals("http://gigapple.files.wordpress.com/2010/10/macbook-feature.png?w=300&h=200", article.getImageUrl());
    }

    @Test
    public void testMashable() throws Exception {
        //String url = "http://mashable.com/2010/08/18/how-tonot-to-ask-someone-out-online/";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("mashable.html")));
        assertTrue(article.getText(), article.getText().startsWith("Imagine, if you will, a crowded dance floor"));
        assertEquals("http://9.mshcdn.com/wp-content/uploads/2010/07/love.jpg", article.getImageUrl());
    }

    @Test
    public void testVenturebeat() throws Exception {
        //String url = "http://social.venturebeat.com/2010/08/18/facebook-reveals-the-details-behind-places/";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("venturebeat.html")));
        assertTrue(article.getText(), article.getText().startsWith("Facebook just confirmed the rumors"));
        assertEquals("http://cdn.venturebeat.com/wp-content/uploads/2010/08/mark-zuckerberg-facebook-places.jpg", article.getImageUrl());
    }

    @Test
    public void testPolitico() throws Exception {
        //String url = "http://www.politico.com/news/stories/1010/43352.html";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("politico.html")));
        assertTrue(article.getText(), article.getText().startsWith("If the newest Census Bureau estimates stay close to form"));
        assertEquals("http://images.politico.com/global/news/100927_obama22_ap_328.jpg", article.getImageUrl());
    }

    @Test
    public void testNinjablog() throws Exception {
        //String url = "http://www.ninjatraderblog.com/im/2010/10/seo-marketing-facts-about-google-instant-and-ranking-your-website/";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("ninjatraderblog.html")));
        assertTrue(article.getText(), article.getText().startsWith("Many users around the world Google their queries"));
    }

    @Test
    public void testSportsillustrated() throws Exception {
        //String url = "http://sportsillustrated.cnn.com/2010/football/ncaa/10/15/ohio-state-holmes.ap/index.html?xid=si_ncaaf";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("sportsillustrated.html")));
        assertTrue(article.getText(), article.getText().startsWith("COLUMBUS, Ohio (AP) -- Ohio State has closed"));
        assertEquals("http://i.cdn.turner.com/si/.e1d/img/4.0/global/logos/si_100x100.jpg",
                article.getImageUrl());
    }

    @Test public void testDailybeast() throws Exception {
        //String url = "http://www.thedailybeast.com/blogs-and-stories/2010-11-01/ted-sorensen-speechwriter-behind-jfks-best-jokes/?cid=topic:featured1";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("thedailybeast.html")));
        assertTrue(article.getText(), article.getText().startsWith("Legendary Kennedy speechwriter Ted Sorensen passed"));
        assertEquals("http://www.tdbimg.com/files/2010/11/01/img-article---katz-ted-sorensen_163531624950.jpg",
                article.getImageUrl());
    }

    @Test
    public void testScience() throws Exception {
        //String url = "http://news.sciencemag.org/sciencenow/2011/04/early-birds-smelled-good.html";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("sciencemag.html")));
        assertTrue(article.getText(), article.getText().startsWith("About 65 million years ago, most of the dinosaurs and many other animals and plants were wiped off Earth, probably due to an asteroid hitting our planet. Researchers have long debated how and why some "));
    }

    @Test
    public void testSlamMagazine() throws Exception {
        //String url = "http://www.slamonline.com/online/nba/2010/10/nba-schoolyard-rankings/";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("slamonline.html")));
        assertTrue(article.getText(), article.getText().startsWith("When in doubt, rank players and add your findings"));
        assertEquals(article.getImageUrl(), "http://www.slamonline.com/online/wp-content/uploads/2010/10/celtics.jpg");
        assertEquals("» NBA Schoolyard Rankings", article.getTitle());
    }

    @Test
    public void testEspn3WithFlashVideo() throws Exception {
        //String url = "http://sports.espn.go.com/nfl/news/story?id=5971053";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("espn3.html")));
        assertTrue(article.getText(), article.getText().startsWith("PHILADELPHIA -- Michael Vick missed practice Thursday"));
        assertEquals("http://a.espncdn.com/i/espn/espn_logos/espn_red.png", article.getImageUrl());
        assertEquals("Michael Vick of Philadelphia Eagles misses practice, unlikely to play vs. Dallas Cowboys", article.getTitle());
    }

    @Test
    public void testSportingNews() throws Exception {
        //String url = "http://www.sportingnews.com/nfl/feed/2011-01/nfl-coaches/story/raiders-cut-ties-with-cable";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("sportingnews.html")));
        assertTrue(article.getText(), article.getText().startsWith("ALAMEDA, Calif. — The Oakland Raiders informed coach Tom Cable on Tuesday that they will not bring him back"));
        assertEquals("http://dy.snimg.com/story-image/0/69/174475/14072-650-366.jpg",
                article.getImageUrl());
        assertEquals("Raiders cut ties with Cable", article.getTitle());
    }

    @Test
    public void testFoxSports() throws Exception {
        //String url = "http://msn.foxsports.com/nfl/story/Tom-Cable-fired-contract-option-Oakland-Raiders-coach-010411";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("foxsports.html")));
        assertTrue(article.getText(), article.getText().startsWith("The Oakland Raiders informed coach Tom Cable"));
        assertEquals("Oakland Raiders won't bring Tom Cable back as coach",
                article.getTitle());
    }

    @Test
    public void testEconomist() throws Exception {
        //String url = "http://www.economist.com/node/17956885";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("economist.html")));
        assertTrue(article.getText(), article.getText().startsWith("FOR beleaguered smokers, the world is an increasingly"));
        assertEquals("http://media.economist.com/images/images-magazine/2011/01/22/st/20110122_stp004.jpg",
                article.getImageUrl());
    }

    @Test
    public void testTheVacationGals() throws Exception {
        //String url = "http://thevacationgals.com/vacation-rental-homes-are-a-family-reunion-necessity/";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("thevacationgals.html")));
        assertTrue(article.getText(), article.getText().startsWith("Editors’ Note: We are huge proponents of vacation rental homes"));
        assertEquals("http://thevacationgals.com/wp-content/uploads/2010/11/Gemmel-Family-Reunion-at-a-Vacation-Rental-Home1-300x225.jpg",
                article.getImageUrl());
    }

    @Test
    public void testShockYa() throws Exception {
        //String url = "http://www.shockya.com/news/2011/01/30/daily-shock-jonathan-knight-of-new-kids-on-the-block-publicly-reveals-hes-gay/";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("shockya.html")));
        assertTrue(article.getText(), article.getText().startsWith("New Kids On The Block singer Jonathan Knight has publicly"));
        assertEquals("http://www.shockya.com/news/wp-content/uploads/jonathan_knight_new_kids_gay.jpg",
                article.getImageUrl());
    }

    @Test
    public void testWikipedia() throws Exception {
        // String url = "http://en.wikipedia.org/wiki/Therapsids";
        // Wikipedia has the advantage of also testing protocol relative URL extraction for Favicon and Images.
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("wikipedia.html")));
        assertTrue(article.getText(), article.getText().startsWith("Therapsida is a group of the most advanced reptile-grade synapsids, and the ancestors of mammals"));
        assertEquals("//upload.wikimedia.org/wikipedia/commons/thumb/4/42/Pristeroognathus_DB.jpg/240px-Pristeroognathus_DB.jpg",
                article.getImageUrl());
        assertEquals("//en.wikipedia.org/apple-touch-icon.png",
                article.getFaviconUrl());
    }

    @Test
    public void testData4() throws Exception {
        // http://blog.traindom.com/places-where-to-submit-your-startup-for-coverage/
        JResult res = extractor.extractContent(readFileAsString("test_data/4.html"));
        assertEquals("36 places where you can submit your startup for some coverage", res.getTitle());
        assertEquals(Arrays.asList("blog coverage", "get coverage", "startup review", "startups", "submit startup"), res.getKeywords());
        assertTrue("data4:" + res.getText(), res.getText().startsWith("So you have a new startup company and want some coverage"));
    }

    @Test
    public void testTimemagazine() throws Exception {
        //String url = "http://www.time.com/time/health/article/0,8599,2011497,00.html";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("time.html")));
        assertTrue(article.getText(), article.getText().startsWith("This month, the federal government released"));
        assertEquals("http://img.timeinc.net/time/daily/2010/1008/bp_oil_spill_0817.jpg", article.getImageUrl());
    }

    @Test
    public void testCnet() throws Exception {
        //String url = "http://news.cnet.com/8301-30686_3-20014053-266.html?tag=topStories1";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("cnet.html")));
        assertTrue(article.getText(), article.getText().startsWith("NEW YORK--Verizon Communications is prepping a new"));
        assertEquals("http://i.i.com.com/cnwk.1d/i/tim//2010/08/18/Verizon_iPad_and_live_TV_610x458.JPG", article.getImageUrl());
    }

    @Test
    public void testBloomberg() throws Exception {
        //String url = "http://www.bloomberg.com/news/2010-11-01/china-becomes-boss-in-peru-on-50-billion-mountain-bought-for-810-million.html";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("bloomberg.html")));
        assertTrue(article.getText(), article.getText().startsWith("The Chinese entrepreneur and the Peruvian shopkeeper"));
        assertEquals("http://www.bloomberg.com/apps/data?pid=avimage&iid=iimODmqjtcQU", article.getImageUrl());
    }

    @Test
    public void testTheFrisky() throws Exception {
        //String url = "http://www.thefrisky.com/post/246-rachel-dratch-met-her-baby-daddy-in-a-bar/";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("thefrisky.html")));
        assertTrue(article.getText(), article.getText().startsWith("Rachel Dratch had been keeping the identity of her baby daddy "));

        assertEquals("http://cdn.thefrisky.com/images/uploads/rachel_dratch_102810_m.jpg",
                article.getImageUrl());
        assertEquals("Rachel Dratch Met Her Baby Daddy At A Bar", article.getTitle());
    }

    @Test
    public void testBrOnline() throws Exception {
        // TODO charset for opera was removed:
        // <![endif]-->
        // <link rel="stylesheet" type="text/x-opera-css;charset=utf-8" href="/css/opera.css" />

        //String url = "http://www.br-online.de/br-klassik/programmtipps/highlight-bayreuth-tannhaeuser-festspielzeit-2011-ID1309895438808.xml";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("br-online.html")));
        assertTrue(article.getText(), article.getText().startsWith("Wenn ein Dirigent, der Alte Musik liebt, erstmals eine "
                + "Neuproduktion bei den Bayreuther Richard-Wagner-Festspielen übernimmt,"));                                                      
        assertEquals("Eröffnung der 100. Bayreuther Festspiele: Alles neu beim \"Tannhäuser\"", 
                article.getTitle());
    }

    @Test
    public void cleanTitle() {
        String title = "Hacker News | Ask HN: Apart from Hacker News, what else you read?";
        assertEquals("Ask HN: Apart from Hacker News, what else you read?", extractor.cleanTitle(title));
        assertEquals("mytitle irgendwas", extractor.cleanTitle("mytitle irgendwas | Facebook"));
        assertEquals("mytitle irgendwas", extractor.cleanTitle("mytitle irgendwas | Irgendwas"));

        // this should fail as most sites do store their name after the post
        assertEquals("mytitle irgendwas", extractor.cleanTitle("Irgendwas | mytitle irgendwas"));
    }

    @Test
    public void testGaltimeWhereUrlContainsSpaces() throws Exception {
        //String url = "http://galtime.com/article/entertainment/37/22938/kris-humphries-avoids-kim-talk-gma";
        JResult article = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("galtime.com.html")));
        assertEquals("http://vnetcdn.dtsph.com/files/vnet3/imagecache/opengraph_ogimage/story-images/Kris%20Humphries%20Top%20Bar.JPG", article.getImageUrl());
    }

    @Test
    public void testIssue8() throws Exception {
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("no-hidden.html")));
        assertEquals("This is the text which is shorter but visible", res.getText());
    }

    @Test
    public void testIssue8False() throws Exception {
        JResult res = extractor.extractContent(c.streamToString(getClass().getResourceAsStream("no-hidden2.html")));
        assertEquals("This is the NONE-HIDDEN text which shouldn't be shown and it is a bit longer so normally prefered", res.getText());
    }

    @Test
    public void testIssue4() throws Exception {
        JResult res = extractor.extractContent("<html><body><div> aaa<a> bbb </a>ccc</div></body></html>");
        assertEquals("aaa bbb ccc", res.getText());

        res = extractor.extractContent("<html><body><div> aaa <strong>bbb </strong>ccc</div></body></html>");
        assertEquals("aaa bbb ccc", res.getText());

        res = extractor.extractContent("<html><body><div> aaa <strong> bbb </strong>ccc</div></body></html>");
        assertEquals("aaa bbb ccc", res.getText());
    }

    /**
     * @param filePath the name of the file to open. Not sure if it can accept URLs or just
     * filenames. Path handling could be better, and buffer sizes are hardcoded
     */
    public static String readFileAsString(String filePath)
            throws java.io.IOException {
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }
}
