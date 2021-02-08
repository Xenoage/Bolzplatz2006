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
echo '<span class="head"><B>Informationen / &Uuml;ber Bolzplatz 2006</B></span><P>';

?>

<BR>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0">
<TR>
<TD VALIGN="Top">
<B>Bolzplatz 2006 ist ein spaßiges Fußballspiel<BR>
im 3D-Comic-Stil für lau.</B>
<UL>
<LI>Kostenlos und Open-Source
<LI>Witzige 3D-Comic-Grafik
<LI>Packende Stadionatmosphäre
<LI>Steuerung mit Tastatur oder Gamepad
<LI>2-Spieler-Modus
<LI>Karriere und Weltmeisterschaft
<LI>Eintrag in die <a href="halloffame.php"><b>Hall of Fame</b></a>
<LI>Baue Dein eigenes Stadion
<P>
<LI>80 Teams
<LI>20 Stadien
<LI>10 Wetterverhältnisse
<LI>50 Werbebanden
<LI>10 Schiedsrichter
<LI>9 Kommentatoren (5xDeutsch, 2xEnglisch, 2xFranzösisch)
<P>
<LI>3 Sprachen: Deutsch, Englisch und Französisch
</UL>

</TD>

<TD>&nbsp;&nbsp;&nbsp;&nbsp;</TD>
<TD VALIGN="Top">
  <!--
  <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0">  <? /* Template für Bilder mit Popup/Grossansicht */ ?>
  <TR>
  <TD BACKGROUND="../global/grafik/ss_schatten.gif"><IMG SRC="../global/grafik/x.gif" width="12" height="12" border="0"></TD>
  <TD><a href="javascript:showPicture('../global/grafik/screenshots/full/spielszene03.jpg','-');"><IMG SRC="../global/grafik/screenshots/preview/spielszene03.jpg" width="250" height="188" border="1"></TD>
  </TR>
  </TABLE>
  -->
<img src="../global/grafik/pose/frontpictwball.gif" align="Right" border="0" width="227" height="230">

</TD>
</TR>
</TABLE>

<BR><B>Systemvoraussetzungen</B>
<BR>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0">
<TR>
<TD VALIGN="Top">Betriebssystem:</TD><TD>&nbsp;&nbsp;&nbsp;</TD>
<TD VALIGN="Top">Windows 2000 oder XP, Linux</TD>
</TR>
<TR>
<TD VALIGN="Top">Prozessor:</TD><TD>&nbsp;&nbsp;&nbsp;</TD>
<TD VALIGN="Top">800 MHz (empfohlen: 1,5 GHz)</TD>
</TR>
<TR>
<TD VALIGN="Top">Arbeitsspeicher:</TD><TD>&nbsp;&nbsp;&nbsp;</TD>
<TD VALIGN="Top">256 MB (empfohlen: 512 MB)</TD>
</TR>
<TR>
<TD VALIGN="Top">Grafikkarte:</TD><TD>&nbsp;&nbsp;&nbsp;</TD>
<TD VALIGN="Top">3D-Grafikkarte mit 64 MB Video-RAM (empfohlen: 128 MB Video-RAM) </TD>
</TR>
<TR>
<TD VALIGN="Top">Festplatte:</TD><TD>&nbsp;&nbsp;&nbsp;</TD>
<TD VALIGN="Top">Mindestens 200 MB freier Speicher</TD>
</TR>
</TABLE>

<BR><BR>

<B>Lizenz</B>
<BR>
Bolzplatz 2006 ist kostenlos und steht unter der General Public License (Programmcode) und Creative Commons Public License (restliche Daten). Das bedeutet, Du darfst das Spiel beliebig kopieren, editieren und kostenlos an andere weitergeben. Gerne darf das Spiel auch auf CDs/DVDs von Spielemagazinen usw. weitergegeben werden, hierbei ist aber zu beachten, dass es noch nicht USK-geprüft ist.<P>
<B>Entwickler</B>
<BR>
Bolzplatz 2006 wurde von einem <a href="team.php">Hobby-Spieleentwickler-Team</a> erstellt. Da es kostenlos verteilt wird, keine sonstigen Einnahmen damit verbunden sind und damit auch in Zukunft solche Projekte möglich sind, sind wir auf Eure finanzielle Unterstützung angewiesen. Ihr könnt Euch die <a href="goldedition.php">Gold-Edition</a> oder <a href="http://bolzplatz2006.spreadshirt.de" TARGET="_blank">T-Shirts</a> kaufen, oder ganz einfach eine beliebige Summe Geld <a href="spend.php">spenden</a>. Vielen Dank!


<?


################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

#mysql_close($db);                                                               # close db
?>
