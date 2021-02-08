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
echo '<span class="head"><B>Informations / Sur Coup de Foot 2006</B></span><P>';

?>

<BR>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0">
<TR>
<TD VALIGN="Top">
<B>Coup de Foot 2006 est un jeu comique en 3D.<BR>
Gratuit et open-source.</B>
<UL>
<LI>Graphiques 3D en style cartoon
<LI>Ambiance de stade presqu'originale
<LI>A commander par clavier ou gamepad
<LI>Mode 2 joueurs
<LI>Mode Carri&egrave;re et Coupe du Monde
<LI>Inscription au <a href="halloffame.php"><b>Hall of Fame</b></a>
<LI>Construis tes propres stades
<P>
<LI>80 &eacute;quipes
<LI>20 stades
<LI>10 conditions atmosph&eacute;riques diff&eacute;rentes
<LI>50 panneaux publicitaires
<LI>10 arbitres
<LI>9 commentateurs
<P>
3 langues: allemand, anglais et fran&ccedil;ais
</UL>

</TD>

<TD>&nbsp;&nbsp;&nbsp;&nbsp;</TD>
<TD VALIGN="Top">
  <!--
  <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0">  <? /* Template f&uuml;r Bilder mit Popup/Grossansicht */ ?>
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

<BR><B>Conditions necessaires</B>
<BR>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0">
<TR>
<TD VALIGN="Top">Syst&egrave;me d&#180;exploitation:</TD><TD>&nbsp;&nbsp;&nbsp;</TD>
<TD VALIGN="Top">Windows 2000 / XP, Linux</TD>
</TR>
<TR>
<TD VALIGN="Top">Processeur:</TD><TD>&nbsp;&nbsp;&nbsp;</TD>
<TD VALIGN="Top">800 MHz</TD>
</TR>
<TR>
<TD VALIGN="Top">M&eacute;moire:</TD><TD>&nbsp;&nbsp;&nbsp;</TD>
<TD VALIGN="Top">256 MB</TD>
</TR>
<TR>
<TD VALIGN="Top">Carte Graphique:</TD><TD>&nbsp;&nbsp;&nbsp;</TD>
<TD VALIGN="Top">3D, 64 MB Video-RAM</TD>
</TR>
<TR>
<TD VALIGN="Top">Disque dur: </TD><TD>&nbsp;&nbsp;&nbsp;</TD>
<TD VALIGN="Top">au moins 200 MB libre</TD>
</TR>
</TABLE>

<BR><BR>

<B>Licence</B>
<BR>
Coup de Foot 2006 est gratuit et soumis aux General Public License (Code du programme) et Creative Communs Public License (reste des informations).Ainsi il t'est permis de copier et &eacute;diter le jeu et de le donner aux autres.<P>
<B>Cr&eacute;ateurs</B>
<BR>
Coup de Foot 2006 &agrave; &eacute;t&eacute; cr&eacute;&eacute; par un groupe de cr&eacute;ateurs de jeux amateurs.
Puisque c&#180;est gratuit, nous d&eacute;pendons de votre aide financi&egrave;re. Vous pouvez
acheter <a href="goldedition.php">Gold Edition</a> ou des <a href="http://bolzplatz2006.spreadshirt.de" TARGET="_blank">T-Shirts</a> de Coup de Foot 2006 ou simplement
<a href="spend.php">nous faire un don</a>. Merci beaucoup d'avance pour votre aide!
<?


################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

#mysql_close($db);                                                               # close db
?>
