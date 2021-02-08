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
echo '<span class="head"><B>Gold Edition</B></span><P>';

?>
Slam Soccer 2006 is free and open source software - and we, the developers, earn no money with it.
<P>
So we rely on your support: For a donation of only 10 Euro you get as a thankyou the Gold-Edition of Slam Soccer 2006 containing a team- and stadium-editor, which allow you to create your own teams and stadiums!
<P>
<CENTER>
<a href="https://order.shareit.com/cart/add?vendorid=200274095&PRODUCT[300789078]=1&languageid=1&backlink=http%3A%2F%2Fwww.bolzplatz2006.de%2Fen%2Fgoldedition.php"><B>&gt;&gt; Order via share-it now! &lt;&lt;</B></a>
<BR><a href="https://order.shareit.com/cart/add?vendorid=200274095&PRODUCT[300789080]=1&languageid=1&backlink=http%3A%2F%2Fwww.bolzplatz2006.de%2Fen%2Fgoldedition.php"><B>&gt;&gt; Linux Version &lt;&lt;</B></a>
</CENTER>
<P>

<b>Team-Editor </b>
<P>
The Team-Editor lets you modify and create new teams:
<P>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="100%">
<TR>
<TD VALIGN="Top">
<LI>Individual player values<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Name<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Position<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Attack, Defense, Speed, Stamina
<LI>Team colors (home, away, goalkeeper) <BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Skin, shirt (with patterns), shorts, shoes <BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Headdress: sunglasses, cap, several hairstyles
<LI>Team logo
<LI>Home stadium
<LI>Adboards
<LI>World cup and career assignment
<LI>Team comparison
</TD>
<TD VALIGN="Top" ALIGN="Right">
  <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0">  <? /* Template für Bilder mit Popup/Grossansicht */ ?>
  <TR>
  <TD BACKGROUND="../global/grafik/ss_schatten.gif"><IMG SRC="../global/grafik/x.gif" width="12" height="12" border="0"></TD>
  <TD align="Center"><a href="javascript:showPicture('../global/grafik/screenshots/full/ge_teameditor01.jpg','-');"><IMG SRC="../global/grafik/screenshots/preview/ge_teameditor01.jpg" width="150" height="113" border="1"><BR>[click]</a></TD>
  </TR>
  </TABLE>
</TD>
</TR>
</TABLE>

<BR><BR>
<b>Stadium-Editor</b>
<P>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="100%">
<TR>
<TD VALIGN="Top">
<LI>Build a stadium without limits (no money, free positioning)
<LI>More than 30 stand elements
<LI>Lawn quality
<LI>Goals
<LI>Adboards
<LI>Scoreboards
<LI>Floodlight
<LI>Attractions
<LI>Place any .x und .ms3d objects

</TD>
<TD VALIGN="Top" ALIGN="Right">
  <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0">  <? /* Template für Bilder mit Popup/Grossansicht */ ?>
  <TR>
  <TD BACKGROUND="../global/grafik/ss_schatten.gif"><IMG SRC="../global/grafik/x.gif" width="12" height="12" border="0"></TD>
  <TD align="Center"><a href="javascript:showPicture('../global/grafik/screenshots/full/ge_stadionedit02.jpg','-');"><IMG SRC="../global/grafik/screenshots/preview/ge_stadionedit02.jpg" width="150" height="113" border="1"><BR>[click]</a></TD>
  </TR>
  </TABLE>
</TD>
</TR>
</TABLE>

<P>
<CENTER>
<a href="https://order.shareit.com/cart/add?vendorid=200274095&PRODUCT[300789078]=1&languageid=1&backlink=http%3A%2F%2Fwww.bolzplatz2006.de%2Fen%2Fgoldedition.php"><B>&gt;&gt; Order via share-it now! &lt;&lt;</B></a>
<BR><a href="https://order.shareit.com/cart/add?vendorid=200274095&PRODUCT[300789080]=1&languageid=1&backlink=http%3A%2F%2Fwww.bolzplatz2006.de%2Fen%2Fgoldedition.php"><B>&gt;&gt; Linux Version &lt;&lt;</B></a>
</CENTER>
<P>


<?


################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

#mysql_close($db);                                                               # close db
?>
