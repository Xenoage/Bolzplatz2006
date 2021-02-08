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

echo '<span class="head"><B>Informations</B></span><P>';

?>
<BR>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0">
<TR>
<TD VALIGN="Top">

<B>Coupe du Monde 2006 en Allemagne!</B><BR>
Et tu n&#180;y participes pas?
<P>
Comment? Tu fais pas du foot?<BR>
Ce n&#180;est pas vrai que chacun a commenc&eacute; t&ocirc;t.
<P>
Monte de la ligue rurale jusqu&#180;&agrave; la coupe du monde et construis toi-m&ecirc;me les stades les plus modernes du monde.
<P>
Si tu sais aussi te d&eacute;brouiller dans le monde du foot et contr&ocirc;ler tes finances, ce n'est qu'un petit pas jusqu'au  <b>Championnat du Coup de Foot 2006!</b>
</TD>

<TD>&nbsp;&nbsp;&nbsp;&nbsp;</TD>
<TD VALIGN="Top">


  <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0">  <? /* Template f&uuml;r Bilder mit Popup/Grossansicht */ ?>
  <TR>
  <TD BACKGROUND="../global/grafik/ss_schatten.gif"><IMG SRC="../global/grafik/x.gif" width="12" height="12" border="0"></TD>
  <TD><a href="javascript:showPicture2('../global/grafik/screenshots/full/spielszene03.jpg','-');"><IMG SRC="../global/grafik/screenshots/preview/spielszene03.jpg" width="250" height="188" border="1"></TD>
  </TR>
  </TABLE>

</TD>


</TR>
</TABLE>

<P>
<B>Plus d&#180;informations:</B>
<UL>
<LI><a href="about.php">Sur Coup de Foot 2006</a>
<LI><a href="screenshots.php">Screenshots (Extraits du jeu)</a>
<LI><a href="development.php">Development</a>
<!--<LI><a href="team.php">Team</a>-->
</UL>


<?
################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

#mysql_close($db);                                                               # close db
?>
