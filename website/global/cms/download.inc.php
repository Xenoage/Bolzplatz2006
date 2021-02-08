<?

    # ====================================================== #
    # BP2k6 - Homepage => Downloads                          #
    # ------------------------------------------------------ #
    # (c)2006 maus / m.schmitz / maussites.de                #
    # LastChange: 23.05.2006                                 #
    # ====================================================== #


# status / todo: o.k.
# ______________________________________________________________________________


#______________________________________________________________________  ####### links erzeugen/anzeigen
function MakeLinks ($FileName,$DownloadURL,$MaxAbweichung,$Text1,$Text2) {
$SendID = $_GET["id"];
if ($SendID == "$FileName")
    {
    $ThisDay = date("z",time());      # heute..:)
    $Checksum = rand(10001, 99999);   # Checksum fuer weitere Berechnungen
    $Trenner1 = $Checksum * 9;        # ..zum einbetten der Checksum
    $Trenner2 = $Checksum * 7;        # ..zum einbetten der Checksum
    $PreChecksum = $Checksum * 1493;  # Checksum um die Checksum zu pruefen ;-)
    $CrDate = $ThisDay * $Checksum * 791;              # CryptDatum
    $CrCheckSum = $Checksum * 137;                     # Crypt Checksum
    $CrLength = $MaxAbweichung * 6458 * $Checksum;     # Crypt Gueltigkeit
    $CrLengthCk = $MaxAbweichung * 3785 * $Checksum;   # Crypt Gueltigkeit / gegenprobe
    $num1 = rand(100001, 999999);     # leFake :)
    $num2 = rand(100001, 999999);     # leFake :)
    $MyID = "$FileName@$PreChecksum,$num1,$CrLength,$CrDate,$CrLengthCk,$Trenner1$CrCheckSum$Trenner2,$num2";
    AddToDB ($FileName);              # in Datenbank eintragen
    echo $Text2.'... <a href="'.$DownloadURL.'?id='.$MyID.'">'."($Text1)</a><P>";
    echo '<html><META HTTP-EQUIV="Refresh" CONTENT="2; URL='.$DownloadURL.'?id='.$MyID.'"></html>';
    }
    else {
         echo '<a href="'.$ThisFile.'?id='.$FileName.'">'.$Text1.'</a><P>';
         }
}

#______________________________________________________________________  ####### in Datenbank eintragen
function AddToDB ($FileName) {
$GetSql = mysql_query("SELECT dl FROM download WHERE Prg='$FileName'");
$XTemp = mysql_fetch_row($GetSql);
$Downloads = $XTemp[0];
if ($Downloads == "")
   {
   mysql_query("INSERT INTO download ( Prg, dl )
                            VALUES ('$FileName', '1' )"
                                   );
   } else {
          $Downloads ++;
          mysql_query("UPDATE download SET dl='$Downloads' WHERE  Prg='$FileName' LIMIT 1");
          }
}

?>