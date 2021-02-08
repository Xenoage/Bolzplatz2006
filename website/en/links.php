<?

    # ====================================================== #
    # BP2k6 - Homepage => News                               #
    # ------------------------------------------------------ #
    # (c)2006 maus / m.schmitz / maussites.de                #
    # LastChange: 02.04.2006                                 #
    # ====================================================== #


# status / todo:  o.k.
# ______________________________________________________________________________

require_once ("_config.inc.php");
# ______________________________________________________________________________

require_once ("../bpadmin/settings.inc.php");
#$db=mysql_connect($dbhost,$dbuser,$dbpass);                                     # open db
#mysql_select_db($dbname);

################################################################# Head ausgeben
require_once ('../global/_head.inc.php');

################################################################# CONTENT
echo '<span class="head"><B>Links</B></span><P>';

?>
<b>Weitere interessante Seiten zum Thema, vom Team empfohlen:</b>&nbsp;&nbsp;&nbsp; ;-)

<UL>

<P><LI><a href="http://www.fairplay-fairlife.de/" TARGET="_blank">www.fairplay-fairlife.de</a>
<BR>Faire Bälle zur WM 2006 sind leider nicht die Regel: Fußbälle, die in deutschen Geschäften verkauft werden, sind zu vier Fünfteln in Pakistan hergestellt worden, und das meist unter menschenunwürdigen Arbeitsbedingungen. Über diese Seite kann man sich fair gehandelte Bälle bestellen und allerlei über den fairen Handel erfahren.

<P><LI><a href="http://www.stadionwelt.de" TARGET="_blank">www.stadionwelt.de</a>
<BR>Stadien, Arenen, Magazin

<P><LI><a href="http://www.fussballdaten.de" TARGET="_blank">www.fussballdaten.de</a>
<BR>Fussballdaten - Die Fußball-Datenbank, Zahlen / Texte / Bilder

<P><LI><a href="http://www.xenoage.com/" TARGET="_blank">www.xenoage.com</a>
<BR>Die offizielle Website von Xenoage Software, dem Entwickler-Team von Bolzplatz 2006, mit Downloads anderer Projekte, z.B. dem Xenoage Java Exe Starter oder Bolzplatz 2000.

<P><LI><a href="http://www.andreaswenger.de" TARGET="_blank">www.andreaswenger.de</a>
<BR>Die private Homepage von Andi mit Verweisen auf seine anderen Projekte.

<P><LI><a href="http://www.milianmusik.de" TARGET="_blank">www.milianmusik.de</a>
<BR>Komponist & Songwriter, verantwortlich für Musik und Sounddesign in Bolzplatz 2006

<P><LI><a href="http://www.pixel-dj.com" TARGET="_blank">www.pixel-dj.com</a>
<BR>Die private Homepage von Markus


</UL>

<!--
<LI><a href="http://" TARGET="_blank">xxxx</a>
<BR>xxxx
-->

<?


################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

#mysql_close($db);                                                               # close db
?>
