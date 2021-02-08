<?

    # ====================================================== #
    # BP2k6 - Homepage => Downloads                          #
    # ------------------------------------------------------ #
    # (c)2006 maus / m.schmitz / maussites.de                #
    # LastChange: 05.06.2006                                 #
    # ====================================================== #


# status / todo:  o.k.
# ______________________________________________________________________________

require_once ("_config.inc.php");
# ______________________________________________________________________________

require_once ("../bpadmin/settings.inc.php");
#$db=mysql_connect($dbhost,$dbuser,$dbpass);                                     # open db
#mysql_select_db($dbname);

$ThisFile = basename($_SERVER["PHP_SELF"]);

################################################################# Head ausgeben
require_once ('../global/_head.inc.php');
require_once ('../global/cms/download.inc.php');

################################################################# CONTENT:
echo '<span class="head"><B>Downloads</B></span><P> <BR>';

?>
<P>Wegen des riesigen Ansturms bitten wir Euch bis auf weiteres die Mirrors zu benutzen.</P>


<B>Windows Versionen:</B>
<UL>

<LI>Download: <b>Bolzplatz 2006 / install.exe</b></a>, ~ 92 MB / Deutsche Version, v1.0.3 <BR>
[<a href="http://prdownloads.sourceforge.net/bp2k6/bolzplatz2006-1.0.3-install.exe?download" TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]
&nbsp;&nbsp;
[<a href="http://files.filefront.com/bolzplatz2006_101_installexe/;5120088;;/fileinfo.html" TARGET="_blank">Mirror#2 (1.0.1) <b>FileFront</b></a>]
<P>
<LI>Download: <b>Slam Soccer 2006 / install.exe</b></a>, ~ 92 MB / Englische Version, v1.0.3 <BR>
[<a href="http://prdownloads.sourceforge.net/bp2k6/slamsoccer2006-1.0.3-install.exe?download" TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]
&nbsp;&nbsp;
[<a href="http://files.filefront.com/Slam_Soccer_2006_101/;5116565;;/fileinfo.html" TARGET="_blank">Mirror#2 (1.0.1) <b>FileFront</b></a>]
<P>
<LI>Download: <b>Coup de Foot 2006 / install.exe</b></a>, ~ 92 MB / Französische Version, v1.0.3 <BR>
[<a href="http://prdownloads.sourceforge.net/bp2k6/coupdefoot2006-1.0.3_install.exe?download" TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]
&nbsp;&nbsp;
[<a href="http://files.filefront.com/Coup_de_foot_2006_101/;5119809;;/fileinfo.html" TARGET="_blank">Mirror#2 (1.0.1) <b>FileFront</b></a>]

</UL>


<B>Linux Versionen:</B>
<UL>


<LI>SuSE-Paket von links2linux.de: <b>Bolzplatz 2006</b>, Mehrsprachige Version, v1.0.3<BR>
[<a href="http://packman.links2linux.de/package/bolzplatz2006"
 TARGET="_blank">Spiel-Paket</a>] [<a href="http://packman.links2linux.de/package/bolzplatz2006-data"
 TARGET="_blank">Daten-Paket</a>] (beides zur Installation erforderlich)
<P>
 <LI>Allgemein (32bit): <b>Bolzplatz 2006 / tar.gz</b></a>, ~ 85 MB / Mehrsprachige Version, v1.0.3<BR>
[<a href="http://prdownloads.sourceforge.net/bp2k6/bolzplatz2006-1.0.3-linux.tar.gz?download"
 TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]

</UL>
<HR SIZE="1"><BR>


<B>Weitere Downloads:</B>
<UL>

<LI>Download: <b>Bolzplatz 2006 Source Code 1.0.3 / zip</b></a>, ~ 5,1 MB <BR>
[<a href="http://prdownloads.sourceforge.net/bp2k6/bolzplatz2006-1.0.3-src.zip?download" TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]
<P>
<LI>Download: <b>Cover DVD-Box / zip</b>, ~ 4,7 MB <BR>
[<a href="http://prdownloads.sourceforge.net/bp2k6/DVD-Box_BP2k6_de.zip?download" TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]
<P>
<LI>Download: <b>3 Wallpapers 1024x768 / zip</b>, ~ 2,1 MB <BR>
[<a href="http://prdownloads.sourceforge.net/bp2k6/bp2k6_wp_1024x768.zip?download" TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]
<P>
<LI>Download: <b>3 Wallpapers 1280x1024 / zip</b>, ~ 2,7 MB <BR>
[<a href="http://prdownloads.sourceforge.net/bp2k6/bp2k6_wp_1280x1024.zip?download" TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]

</UL>

<p><HR SIZE="1"><BR>
<P>


<B>Presse-Bereich</B>

<UL>

<LI>Download: <b>Logos + Screenshots</b> / zip Logo in 3 Sprachen, 7 Screenshots, ~5,6MB <BR>
[<a href="http://prdownloads.sourceforge.net/bp2k6/pressematerial.zip?download" TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]

</UL>


<?

################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

#mysql_close($db);                                                               # close db


?>
