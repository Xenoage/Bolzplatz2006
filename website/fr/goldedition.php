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
Coup de Foot 2006 est logiciel libre - gratuit et open source.
<P>
Nous, les d&eacute;veloppeurs n'en recevons pas de paiement. C'est pour &ccedil;a que nous d&eacute;pendons de vos aides. Pour un don de 10 Euros, nous vous remercions avec la Gold Edition de Coup de Foot 2006 contenant en &eacute;diteur, qui vous permettra de former vos propres stades et &eacute;quipes. <P>
<CENTER>
<a href="https://order.shareit.com/cart/add?vendorid=200274095&PRODUCT[300789079]=1&languageid=6&backlink=http%3A%2F%2Fwww.bolzplatz2006.de%2Ffr%2Fgoldedition.php"><B>&gt;&gt; Commander la Gold Edition avec share-it! &lt;&lt;</B></a>
<BR><a href="https://order.shareit.com/cart/add?vendorid=200274095&PRODUCT[300789080]=1&languageid=6&backlink=http%3A%2F%2Fwww.bolzplatz2006.de%2Ffr%2Fgoldedition.php"><B>&gt;&gt; Linux Version &lt;&lt;</B></a>
</CENTER>
<P>

<b>Editeur d'&eacute;quipes </b>
<P>
Avec l'&eacute;diteur d'&eacute;quipes vous pouvez former vos propres &eacute;quipes et &eacute;diter les &eacute;quipes originales:
<P>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="100%">
<TR>
<TD VALIGN="Top">
<LI>les valeurs individuelles des joueurs <BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- les noms des joueurs <BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- leurs fonctions dans l'&eacute;quipe<BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- force d'attaque, de d&eacute;fense, v&eacute;locit&eacute; et endurance
<LI>les couleurs de l'&eacute;quipe (domiciles, ext&eacute;rieures, gardien de but) <BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- la peau, les maillots (unis ou ray&eacute;s), shorts, chaussures <BR>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;- d&eacute;coration de la t&ecirc;te: lunettes, casquette, coiffures diverses
<LI>&eacute;tablir un embl&egrave;me d'&eacute;quipe propre
<LI>le stade domicile
<LI>les panneaux publicitaires
<LI>association aux ligues (mode carri&egrave;re) ou &agrave; la coupe du monde
<LI>classement et comparaison de toutes les &eacute;quipes
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
<b>Editeur de stades</b>
<P>

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="100%">
<TR>
<TD VALIGN="Top">
<LI>construir ton propre stade sans restrictions financi&egrave;res, &agrave; poser librement
<LI>plus de 30 &eacute;l&eacute;ments de tribunes
<LI>qualit&eacute; de la pelouse
<LI>buts divers
<LI>panneaux publicitaires
<LI>tableaux d'affichage
<LI>illumination, &eacute;clairage par projecteurs
<LI>divertissements
<LI>position d'objets .x et .ms3d quelconques

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
<a href="https://order.shareit.com/cart/add?vendorid=200274095&PRODUCT[300789079]=1&languageid=6&backlink=http%3A%2F%2Fwww.bolzplatz2006.de%2Ffr%2Fgoldedition.php"><B>&gt;&gt; Commander la Gold Edition avec share-it! &lt;&lt;</B></a>
<BR><a href="https://order.shareit.com/cart/add?vendorid=200274095&PRODUCT[300789080]=1&languageid=6&backlink=http%3A%2F%2Fwww.bolzplatz2006.de%2Ffr%2Fgoldedition.php"><B>&gt;&gt; Linux Version &lt;&lt;</B></a>
</CENTER>
<P>

<?


################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

#mysql_close($db);                                                               # close db
?>
