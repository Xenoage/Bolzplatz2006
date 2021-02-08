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
echo '<span class="head"><B>Forums</B></span><P>';

?>
In the <b>Slam Soccer 2006 discussion board</b> you can find many questions and answers to the game.
<P>
If you want to join the discussion, you have to register before.<BR>
<P>
Please take a look in the <a href="faq.php"><B>&gt;FAQ&lt;</B></a> first, perhaps there is already a solution for your problem.
<P> <BR>
<UL>
<LI><span class="head"><a href="../forum/index.php?c=44" TARGET="_blank"><B>Open discussion board</B></a></span>
<P>
<LI><a href="../forum/profile.php?mode=register" TARGET="_blank">Register</a>
</UL>

<BR>


<?


################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

#mysql_close($db);                                                               # close db
?>
