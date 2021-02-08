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
echo '<span class="head"><B>T-Shirts</B></span><P>';

?>

Unfortunately we had to close our t-shirt-shop.<br/><br/>

Our service partner had to remove all products with images looking similar to the logo of the world cup, because of some legal disputes with the organizers of the competition.<br/><br/>

If you want to buy a t-shirt anyway, please send us an <a href="mailto:info@xenoage.com">e-mail</a>.

<br/><br/>

<?


################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

#mysql_close($db);                                                               # close db
?>
