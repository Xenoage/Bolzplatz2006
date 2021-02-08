<?

    # ====================================================== #
    # BP2k6 - Homepage => Downloads                          #
    # ------------------------------------------------------ #
    # (c)2006 maus / m.schmitz / maussites.de                #
    # LastChange: 30.05.2006                                 #
    # ====================================================== #


# status / todo:  o.k.
# ______________________________________________________________________________

require_once ("_config.inc.php");
# ______________________________________________________________________________

require_once ("../bpadmin/settings.inc.php");
$db=mysql_connect($dbhost,$dbuser,$dbpass);                                     # open db
mysql_select_db($dbname);

$ThisFile = basename($_SERVER["PHP_SELF"]);

################################################################# Head ausgeben
require_once ('../global/_head.inc.php');
require_once ('../global/cms/download.inc.php');

################################################################# CONTENT:
echo '<span class="head"><B>Downloads</B></span><P> <BR>';

?>
<P>Wegen des riesigen Ansturms bitten wir Euch bis auf weiteres die Sourceforge Mirror zu benutzen.</P>
<B>Windows Versionen:</B>
<P>
<!-- ORIGINAL
<LI>Download: <a href="http://download.xenoage.com:81/download.php?filenumber=0"><b>Bolzplatz 2006 / install.exe</b></a>, ~ 92 MB / Deutsche Version &nbsp;&nbsp;[<a href="http://prdownloads.sourceforge.net/bp2k6/bolzplatz2006-install.exe?download" TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]
<LI>Download: <a href="http://download.xenoage.com:81/download.php?filenumber=1"><b>Slam Soccer 2006 / install.exe</b></a>, ~ 92 MB / Englische Version &nbsp;&nbsp;[<a href="http://prdownloads.sourceforge.net/bp2k6/slamsoccer2006-install.exe?download" TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]
<LI>Download: <a href="http://download.xenoage.com:81/download.php?filenumber=2"><b>Coup de Foot 2006 / install.exe</b></a>, ~ 92 MB / Französische Version &nbsp;&nbsp;[<a href="http://prdownloads.sourceforge.net/bp2k6/coupdefoot2006-install.exe?download" TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]
<P>
<B>Linux Versionen:</B>
<P>
<LI>Download: <a href="http://download.xenoage.com:81/download.php?filenumber=3"><b>Bolzplatz 2006 / tar.gz</b></a>, ~ 85 MB / Mehrsprachige Version &nbsp;&nbsp;[<a href="http://prdownloads.sourceforge.net/bp2k6/bolzplatz2006-linux.tar.gz?download" TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]
<p><HR SIZE="1"><BR>
<B>Weitere Downloads:</B>
<P>
<LI>Download: <a href="http://download.xenoage.com:81/download.php?filenumber=8"><b>Bolzplatz 2006 Source Code / zip</b></a>, ~ 5,1 MB &nbsp;&nbsp;[<a href="http://prdownloads.sourceforge.net/bp2k6/bolzplatz2006-src.zip?download" TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]
<P>
<LI>Download: <a href="http://download.xenoage.com:81/download.php?filenumber=4"><b>Cover DVD-Box / zip</b></a>, ~ 4,7 MB
<LI>Download: <a href="http://download.xenoage.com:81/download.php?filenumber=5"><b>3 Wallpapers 1024x768 / zip</b></a>, ~ 2,1 MB
<LI>Download: <a href="http://download.xenoage.com:81/download.php?filenumber=6"><b>3 Wallpapers 1280x1024 / zip</b></a>, ~ 2,7 MB


<p><HR SIZE="1"><BR>
<P>
<B>Presse-Bereich</B>
<P>
<LI>Download: <a href="http://download.xenoage.com:81/download.php?filenumber=7"><b>Logos + Screenshots</b> / zip</a> Logo in 3 Sprachen, 7 Screenshots, ~5,6MB
--!>
<!-- Version nur mit Sourceforge -->
<LI>Download: <b>Bolzplatz 2006 / install.exe</b></a>, ~ 92 MB / Deutsche Version &nbsp;&nbsp;[<a href="http://prdownloads.sourceforge.net/bp2k6/bolzplatz2006-1.0.1-install.exe?download" TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]
<LI>Download: <b>Slam Soccer 2006 / install.exe</b></a>, ~ 92 MB / Englische Version &nbsp;&nbsp;[<a href="http://prdownloads.sourceforge.net/bp2k6/slamsoccer2006-1.0.1-install.exe?download" TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]
<LI>Download: <b>Coup de Foot 2006 / install.exe</b></a>, ~ 92 MB / Französische Version &nbsp;&nbsp;[<a href="http://prdownloads.sourceforge.net/bp2k6/coupdefoot2006-1.0.1-install.exe?download" TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]
<P>
<B>Linux Versionen:</B>
<P>
<LI>Download: <b>Bolzplatz 2006 / tar.gz</b></a>, ~ 85 MB / Mehrsprachige Version &nbsp;&nbsp;[<a href="http://prdownloads.sourceforge.net/bp2k6/bolzplatz2006-linux.tar.gz?download" TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]
<p><HR SIZE="1"><BR>
<B>Weitere Downloads:</B>
<P>
<LI>Download: <b>Bolzplatz 2006 Source Code / zip</b></a>, ~ 5,1 MB &nbsp;&nbsp;[<a href="http://prdownloads.sourceforge.net/bp2k6/bolzplatz2006-src.zip?download" TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]
<P>
<LI>Download: <b>Cover DVD-Box / zip</b>, ~ 4,7 MB &nbsp;&nbsp;[<a href="http://prdownloads.sourceforge.net/bp2k6/DVD-Box_BP2k6_de.zip?download" TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]
<LI>Download: <b>3 Wallpapers 1024x768 / zip</b>, ~ 2,1 MB &nbsp;&nbsp;[<a href="http://prdownloads.sourceforge.net/bp2k6/bp2k6_wp_1024x768.zip?download" TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]
<LI>Download: <b>3 Wallpapers 1280x1024 / zip</b>, ~ 2,7 MB &nbsp;&nbsp;[<a href="http://prdownloads.sourceforge.net/bp2k6/bp2k6_wp_1280x1024.zip?download" TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]


<p><HR SIZE="1"><BR>
<P>
<B>Presse-Bereich</B>
<P>
<LI>Download: <b>Logos + Screenshots</b> / zip Logo in 3 Sprachen, 7 Screenshots, ~5,6MB &nbsp;&nbsp;[<a href="http://prdownloads.sourceforge.net/bp2k6/pressematerial.zip?download" TARGET="_blank">Mirror#1 <b>Sourceforge</b></a>]
<?

################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

mysql_close($db);                                                               # close db


?>
