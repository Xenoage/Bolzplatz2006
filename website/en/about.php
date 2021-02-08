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
echo '<span class="head"><B>Information / About Slam Soccer 2006</B></span><P>';

?>

<BR>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0">
<TR>
<TD VALIGN="Top">
<B>Slam Soccer 2006 is a funny football game in<BR>
3D-comic-style - and it's for free!</B>
<UL>
<LI>Freeware and open source
<LI>Funny 3d-comic-style
<LI>Enthralling stadium atmosphere
<LI>Keyboard and gamepad control
<LI>2-player mode
<LI>Career and world cup
<LI>Register in the  <a href="halloffame.php"><b>online hall of fame</b></a>
<LI>Build your own stadium
<P>
<LI>80 teams
<LI>20 stadiums
<LI>10 weather conditions
<LI>50 adboards
<LI>10 referees
<LI>9 commentators (5 German, 2 English, 2 French)
<P>
<LI>3 languages: German, English, French
</UL>
<b>And much more! </b>
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

<BR><B>System requirements</B>
<BR>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0">
<TR>
<TD VALIGN="Top">Operating system:</TD><TD>&nbsp;&nbsp;&nbsp;</TD>
<TD VALIGN="Top">Windows 2000 or XP, Linux</TD>
</TR>
<TR>
<TD VALIGN="Top">CPU:</TD><TD>&nbsp;&nbsp;&nbsp;</TD>
<TD VALIGN="Top">800 MHz (recommended: 1,5 GHz)</TD>
</TR>
<TR>
<TD VALIGN="Top">RAM:</TD><TD>&nbsp;&nbsp;&nbsp;</TD>
<TD VALIGN="Top">256 MB (empfohlen: 512 MB)</TD>
</TR>
<TR>
<TD VALIGN="Top">Video card:</TD><TD>&nbsp;&nbsp;&nbsp;</TD>
<TD VALIGN="Top">3D video card with 64 MB video-RAM (recommended: 128 MB video-RAM)</TD>
</TR>
<TR>
<TD VALIGN="Top">Hard drive:</TD><TD>&nbsp;&nbsp;&nbsp;</TD>
<TD VALIGN="Top">At least 200 MB free memory</TD>
</TR>
</TABLE>

<BR><BR>

<B>License</B>
<BR>
Slam Soccer 2006 is freeware and is provided under the General Public License (program code) and the Creative Commons Public License (remaining data). That means, you may copy, modify and redistribute the game for free. Of course the game can also be distributed on CDs or DVDs (e.g. with game magazines) as far as the law of your country allows that.<P>
<B>Developers</B>
<BR>
Slam Soccer 2006 was created by a <a href="team.php">team of hobby game developers</a>.
Because it is distributed for free, we need your voluntary support! You can buy the <a href="goldedition.php">Gold Edition</a> or <a href="http://bolzplatz2006.spreadshirt.de" TARGET="_blank">T-Shirts</a>
 or you can <a href="spend.php">donate money</a>. Thank you very much!
<?


################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

#mysql_close($db);                                                               # close db
?>
