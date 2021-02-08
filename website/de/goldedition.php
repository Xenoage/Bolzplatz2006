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
Bolzplatz 2006 ist freie Software: Kostenlos und Open Source - auch für uns Entwickler ohne Bezahlung.
<P>
Deshalb sind wir auf Eure Unterstützung angewiesen: Für eine Spende von 10 Euro bedanken wir uns mit der Gold-Edition von Bolzplatz 2006, die einen Team- und Stadioneditor enthält, mit dem Ihr Eure Mannschaften und Stadien frei gestalten könnt.
<P>
<CENTER>
<a href="https://order.shareit.com/cart/add?vendorid=200274095&PRODUCT[300789074]=1&languageid=2&backlink=http%3A%2F%2Fwww.bolzplatz2006.de%2Fde%2Fgoldedition.php"><B>&gt;&gt; Jetzt bei share-it bestellen! &lt;&lt;</B></a>
<BR><a href="https://order.shareit.com/cart/add?vendorid=200274095&PRODUCT[300789080]=1&languageid=2&backlink=http%3A%2F%2Fwww.bolzplatz2006.de%2Fde%2Fgoldedition.php"><B>&gt;&gt; Linux Version &lt;&lt;</B></a>
</CENTER>
<P>

<b>Team-Editor </b>
<P>
Mit dem Team-Editor könnt Ihr eigene Mannschaften einrichten bzw. vorhandene Mannschaften bearbeiten:
<P>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="100%">
<TR>
<TD VALIGN="Top">
<LI>Individuelle Spielerwerte<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Name<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Position<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Angriff, Abwehr, Geschwindigkeit, Ausdauer
<LI>Teamfarben (Heim, Auswärts, Torwart)<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Haut, Shirt (mit Mustern), Hose, Schuhe<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- Kopfbedeckung: Brille, Cap, verschiedene Frisuren
<LI>Teamlogo
<LI>Heimstadion
<LI>Werbebanden
<LI>Zuordnung zu WM und Karriere
<LI>Teamvergleich
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
<b>Stadion-Editor (jetzt auch für Linux!)</b>
<P>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="100%">
<TR>
<TD VALIGN="Top">
<LI>Stadion bauen ohne Geld-Beschränkungen
<LI>Frei platzierbare Objekte/Tribünenteile
<LI>Mehr als 30 Tribünenteile
<LI>Rasenqualität
<LI>Verschiedene Tore
<LI>Werbebanden
<LI>Anzeigetafeln
<LI>Flutlicht
<LI>Attraktionen
<LI>Platzierung von beliebigen .x und .ms3d Objekten

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
<a href="https://order.shareit.com/cart/add?vendorid=200274095&PRODUCT[300789074]=1&languageid=2&backlink=http%3A%2F%2Fwww.bolzplatz2006.de%2Fde%2Fgoldedition.php"><B>&gt;&gt; Jetzt bei share-it bestellen! &lt;&lt;</B></a>
<BR><a href="https://order.shareit.com/cart/add?vendorid=200274095&PRODUCT[300789080]=1&languageid=2&backlink=http%3A%2F%2Fwww.bolzplatz2006.de%2Fde%2Fgoldedition.php"><B>&gt;&gt; Linux Version &lt;&lt;</B></a>
</CENTER>
<P>

<?


################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

#mysql_close($db);                                                               # close db
?>
