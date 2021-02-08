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
echo '<span class="head"><B>Kontakt</B></span><P>';

?>
<img src="../global/grafik/pose/frontballplay.gif" border="0" align="Right">
<UL><BR><BR>
<span class="head"><a href="http://www.xenoage.com" TARGET="_blank"><B>Xenoage Software</B></span><BR>
www.xenoage.com</a>
<P>
Andreas Wenger<BR>
Schleifmühlweg 1<BR>
86529 Schrobenhausen<BR>
Deutschland
<P>
E-Mail: info<!-- aetsch you spambot -->(<!-- aetsch you spambot -->at)x<!-- aetsch you spambot -->enoage.c<!-- aetsch you spambot -->om
</UL>



<?


################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

#mysql_close($db);                                                               # close db
?>
