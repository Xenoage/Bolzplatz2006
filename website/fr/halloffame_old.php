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
$htext06 = "Retourner &agrave; Hall of Fame";
$htext12 = "Tous les champions de: ";
$htext20 = "Coupe du monde";
$htext21 = "Ligue Allstars";

if ($_GET["u"])
             {
             require_once ('../global/cms/hof-userlands.inc.php');
             }
   else {
?>

<BR><UL>
<LI><a href="wm.php">Montrer tous les champions  de la coupe du monde</a>
<LI><a href="allstarsleague.php">Montrer tous les champions  de la ligue allstars</a>
</UL>


<BR><BR><HR SIZE="1"><?}?>

<BR>
<CENTER>
<B>Joueurs des pays suivants se sont déjà inscrit:</B><BR>
(cliquez sur les pays)<BR>

<?
################################################################# Flaggen ausgeben

$GetUsrSql = mysql_query("SELECT * FROM hof_user_wc ORDER BY Land");
if (mysql_num_rows($GetUsrSql) > 0)
    {
    echo "<BR><B>Coupe du monde:</B><BR>";
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

$GetUsrSql = mysql_query("SELECT * FROM hof_user ORDER BY Land");
if (mysql_num_rows($GetUsrSql) > 0)
    {
    echo "<BR><B>Ligue Allstars:</B><BR>";
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
