<?
header('Content-type: text/html; charset=windows-1252');

    # ====================================================== #
    # BP2k6 - Zugang DB, SubConfigs                          #
    # ------------------------------------------------------ #
    # (c)2006 maus / m.schmitz / maussites.de                #
    # LastChange: 30.03.2006                                 #
    # ====================================================== #
// Angaben Datenbank
// -------------------------------------
$dbhost = "TODO"; $dbuser = "TODO"; $dbpass = "TODO"; $dbname = "TODO";
#Datenbankdaten fuer FORUM
$dbphpbb2 = "TODO";
$dbphpbb2_user = "TODO";
$dbphpbb2_pass = "TODO";
// SubConfigs
// -------------------------------------
## DOWNLOADS
# Downloadserver(pfad)/scriptname.php (_eigener_ Server)
$DownloadURL = "http://download.xenoage.com/downloadfiles/getdownload.php";  # Downloadserver(pfad)/scriptname.php
# Gueltigkeit in Tagen:  (0 = nur heute, 1 = bis morgen, usw...)
$MaxAbweichung = "2";
// -------------------------------------
$DebugMode = 0;                           ### Zeigs mir... :) Vorsicht: haessliche Ausgaben.. ;D
// -------------------------------------
$NeedForum = 1;  # don't change !!
?>
