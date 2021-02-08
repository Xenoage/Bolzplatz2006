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

Leider mussten wir unseren T-Shirt-Shop offline nehmen.<br/><br/>

Hintergrund ist eine Vereinbarung zwischen unserem Dienstleister und dem Veranstalter der WM, die eine Verbreitung von WM-Logo-ähnlichen Motiven verbietet (darunter scheint auch unser Logo zu fallen). Wir bedauern diese Entscheidung, und wollen auch weiterhin mit unserem Spiel, das freie Software ist und immer bleiben wird, ein Zeichen gegen den Kommerz im Fußball setzen!<br/><br/>

Wer trotzdem noch ein T-Shirt ergattern will, kann uns gerne eine <a href="mailto:info@xenoage.com">E-Mail</a> schreiben. Wir finden sicher einen Weg, z.B. über andere Dienstleister.

<br/><br/>

<?


################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

#mysql_close($db);                                                               # close db
?>
