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
$ThisFile = basename($_SERVER["PHP_SELF"]);
$db=mysql_connect($dbhost,$dbuser,$dbpass);                                     # open db
mysql_select_db($dbname);

################################################################# Head ausgeben
require_once ('../global/_head.inc.php');

################################################################# CONTENT
echo '<span class="head"><B>Hall of Fame</B></span><P>';
$htext06 = "Zurück zu Hall of Fame";
$htext12 = "Alle Gewinner aus: ";
$htext20 = "Weltmeisterschaft";
$htext21 = "Allstars League";

if ($_GET["u"])
             {
             require_once ('../global/cms/hof-userlands.inc.php');
             }
   else {
?>

<BR><UL>
<LI><a href="wm.php">Zeige alle WM-Gewinner</a>
<LI><a href="allstarsleague.php">Zeige alle Allstars League-Gewinner</a>
</UL>


<BR><BR><HR SIZE="1"><?}?>

<BR>
<CENTER>
<B>Teilnehmer folgender Länder haben sich bisher eingetragen:</B><BR>
(einfach auf die Länder klicken)<BR>

<?
################################################################# Flaggen ausgeben

$GetUsrSql = mysql_query("SELECT * FROM hof_user_wc ORDER BY Land");
if (mysql_num_rows($GetUsrSql) > 0)
    {
    echo "<BR><B>Weltmeisterschaft:</B><BR>";
    while ($MyUser = mysql_fetch_row($GetUsrSql))
           {
           if (($MyUser[4] != $LastFlag) and ($MyUser[4] != ""))
                {
                echo "<a href=\"$ThisFile"."?u=".$MyUser[4]."\"><IMG SRC=\"../global/grafik/flags/$MyUser[4].gif\" border=0 hspace=4 vspace=4></a>";
                }
           $LastFlag = $MyUser[4];
           }
    echo "<BR>";
    }

$LastFlag = "";
$GetUsrSql = mysql_query("SELECT * FROM hof_user ORDER BY Land");
if (mysql_num_rows($GetUsrSql) > 0)
    {
    echo "<BR><B>Allstars League:</B><BR>";
    while ($MyUser = mysql_fetch_row($GetUsrSql))
           {
           if (($MyUser[4] != $LastFlag) and ($MyUser[4] != ""))
                {
                echo "<a href=\"$ThisFile"."?u=".$MyUser[4]."\"><IMG SRC=\"../global/grafik/flags/$MyUser[4].gif\" border=0 hspace=4 vspace=4></a>";
                }
           $LastFlag = $MyUser[4];
           }
    echo "<BR>";
    }

echo '<BR><BR><HR SIZE="1"></CENTER>';

################################################################# Footer ausgeben
require_once ('../global/_footer.inc.php');

mysql_close($db);                                                               # close db
?>
