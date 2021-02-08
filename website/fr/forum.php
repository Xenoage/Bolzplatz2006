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
echo '<span class="head"><B>Forum</B></span><P>';

?>
Le forum de <b>Coup de Foot 2006</b> r&eacute;pond &agrave; toutes les questions sur le jeu.
<P>
Pour pouvoir poser des questions dans le forum, on doit s'y enregistrer d'abord.
<BR>
Le  forum est disponible en toutes les langues support&eacute;es par Coup de Foot 2006.
<P>
S&#180;il te pla&icirc;t, v&eacute;rifie les <a href="faq.php"><B>&gt;FAQ&lt;</B></a> avant de poser des questions.
<P> <BR>
<UL>
<LI><span class="head"><a href="../forum/index.php?c=44" TARGET="_blank"><B>Au forum...</B></a></span>
<P>
<LI><a href="../forum/profile.php?mode=register" TARGET="_blank">Enregistrer d'abord</a>
</UL>

<BR>


<?


################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

#mysql_close($db);                                                               # close db
?>
