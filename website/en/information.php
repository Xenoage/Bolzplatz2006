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

echo '<span class="head"><B>Information</B></span><P>';

?>
<BR>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0">
<TR>
<TD VALIGN="Top">

<B>Football World Cup 2006 in Germany!</B><BR>
What, you are not taking part?
<P>
You can not play football at all?<BR>
As if! Everybody started from scratch.
<P>
You begin in the muddy village league and work your way up to the international top class of football and build up your own stadium!
<P>
Now it's up to you: If you also find your way in the dubious business of football and if you get your finances under control, it's just a matter of your skill until you win the cup and are the <b>Slam Soccer World Champion 2006</b>!
</TD>

<TD>&nbsp;&nbsp;&nbsp;&nbsp;</TD>
<TD VALIGN="Top">


  <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0">  <? /* Template für Bilder mit Popup/Grossansicht */ ?>
  <TR>
  <TD BACKGROUND="../global/grafik/ss_schatten.gif"><IMG SRC="../global/grafik/x.gif" width="12" height="12" border="0"></TD>
  <TD><a href="javascript:showPicture2('../global/grafik/screenshots/full/spielszene03.jpg','-');"><IMG SRC="../global/grafik/screenshots/preview/spielszene03.jpg" width="250" height="188" border="1"></TD>
  </TR>
  </TABLE>

</TD>


</TR>
</TABLE>

<P>
<B>More information:</B>
<UL>
<LI><a href="about.php">About Slam Soccer 2006</a>
<LI><a href="screenshots.php">Screenshots</a>
<LI><a href="development.php">Development</a>
<LI><a href="team.php">Team</a>
</UL>


<?
################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

#mysql_close($db);                                                               # close db
?>
