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
Im <B>Bolzplatz 2006 Forum</B> findest Du viele Fragen und Antworten rund um das Spiel und Technik.
<P>
Wenn Du selber Fragen stellen willst, musst Du Dich erst Registrieren [Register].<BR>
<P>
Dort kannst Du dann auch festlegen, in welcher Grundsprache (Deutsch, Englisch, Französisch)<BR>
das Forum dargestellt wird (Standard = Englisch).
<P>
Bitte schau zuerst in den <a href="faq.php"><B>&gt;FAQ&lt;</B></a> nach, ob Dein Problem schon bekannt ist und wie es zu lösen ist.
<P> <BR>
<UL>
<LI><span class="head"><a href="../forum/index.php?c=44" TARGET="_blank"><B>Forum jetzt aufrufen</B></a></span>
<P>
<LI><a href="../forum/profile.php?mode=register" TARGET="_blank">Direkt zur Registrierung</a>
</UL>

<BR>


<?


################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

#mysql_close($db);                                                               # close db
?>