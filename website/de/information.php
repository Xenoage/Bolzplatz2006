<?

    # ====================================================== #
    # BP2k6 - Homepage => Informationen                      #
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


################################################################# CONTENT:

echo '<span class="head"><B>Informationen</B></span><P>';

?>
<BR>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0">
<TR>
<TD VALIGN="Top">

<B>Fuﬂball-Weltmeisterschaft 2006 in Deutschland!</B><BR>
Und Du bist nicht dabei?
<P>
Wie, Du kannst gar nicht Fuﬂball spielen?<BR>Von wegen, jeder hat mal klein angefangen.<P>
Schieﬂ Dich von der Dorfliga hoch bis in die internationale Spitzenklasse und baue Deinen Hinterhof aus zu einem der modernsten Stadien der Welt!
<P>
Wenn Du Dich jetzt noch in den mafiˆsen Strukturen der Fuﬂballwelt zurechtfindest und Deine Finanzen im Griff hast, fehlt nur noch ein bisschen Geschicklichkeit und Einfallsreichtum, bis Du nach dem groﬂen Finale den Pokal in den H‰nden h‰ltst: <B>Bolzplatz-Weltmeister 2006!</B>

</TD>

<TD>&nbsp;&nbsp;&nbsp;&nbsp;</TD>
<TD VALIGN="Top">


  <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0">  <? /* Template f¸r Bilder mit Popup/Grossansicht */ ?>
  <TR>
  <TD BACKGROUND="../global/grafik/ss_schatten.gif"><IMG SRC="../global/grafik/x.gif" width="12" height="12" border="0"></TD>
  <TD><a href="javascript:showPicture2('../global/grafik/screenshots/full/spielszene03.jpg','-');"><IMG SRC="../global/grafik/screenshots/preview/spielszene03.jpg" width="250" height="188" border="1"></TD>
  </TR>
  </TABLE>

</TD>


</TR>
</TABLE>

<P>
<B>Mehr zum Thema:</B>
<UL>
<LI><a href="about.php">&Uuml;ber Bolzplatz 2006</a>
<LI><a href="screenshots.php">Screenshots</a>
<LI><a href="development.php">Entwicklung</a>
<LI><a href="team.php">Team</a>
</UL>


<?
################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

#mysql_close($db);                                                               # close db
?>
