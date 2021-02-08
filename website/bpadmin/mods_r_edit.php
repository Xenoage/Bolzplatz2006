<?

    # ====================================================== #
    # BP2k6 - Admin -> AddMods                               #
    # ------------------------------------------------------ #
    # (c)2006 maus / m.schmitz / maussites.de                #
    # LastChange: 22.07.2006                                 #
    # ====================================================== #


# status / todo:  o.k.
# ______________________________________________________________________________


?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2//EN">
<HTML>
<HEAD>
<link rel=stylesheet type=text/css href=bpadmin.css>
</HEAD>
<BODY>

<CENTER>
<TABLE BORDER="0" CELLSPACING="1" CELLPADDING="7" class="outertablebg" WIDTH="650" HEIGHT="92%">
<TR><TD VALIGN="Top" HEIGHT="10">
<span class="head"><B>BP2k6</B> -&gt; Admin -&gt; Mods -&gt; Rubriken / Einträge</B></span>
</TD></TR>
<TR><TD VALIGN="Top" HEIGHT="5" class="marktabletd">
<? require_once ('nav.inc.php'); ?>
</TD></TR>

<TR><TD class="tabletd" VALIGN="Top" HEIGHT="99%">

<?
require_once ("settings.inc.php");
$db=mysql_connect($dbhost,$dbuser,$dbpass);                                     # open db
mysql_select_db($dbname);


$donow = $_POST["do"];
$dowhat = $_POST["VarID"];
$CngID = $_POST["CngID"];
$newrbr = $_POST["newrbr"];
$oldrbr = $_POST["oldrbr"];
$Filename = $_POST["Filename"];
$bez_de = $_POST["bez_de"];
$bez_en = $_POST["bez_en"];
$bez_fr = $_POST["bez_fr"];

if ($donow == "addnewrbr")                                                      # Neue Kat anlegen
     {
     if (mysql_num_rows(mysql_query("SELECT * FROM mods2_rubrik WHERE ID='$CngID'")) != 1)
     {
     if (($CngID != "")and($bez_de != "")and($bez_en != "")and($bez_fr != ""))
          {
          mysql_query("INSERT INTO mods2_rubrik (ID, Bez_DE, Bez_EN, Bez_FR)
            VALUES ('$CngID', '$bez_de', '$bez_en', '$bez_fr' )"
            );
           mkdir ("../download/mods/$CngID", 0777);
           mkdir ("../global/grafik/mods/$CngID", 0777);
           }
          else
              {
              echo '<B><BR><HR SIZE="1">[!] FEHLER: Nicht alle Felder für neue Kategorie ausgefuellt !</B><HR SIZE="1"><P>';
              }
     }
      else {
           echo '<B><BR><HR SIZE="1">[!] FEHLER: ID der Kategorie ist bereits vorhanden !</B><HR SIZE="1"><P>';
           }

     $donow = "";
     }


if ($donow == "editrbrname")                                                      # Kat edit
     {
     $MyEntry = mysql_query("SELECT * FROM mods2_rubrik WHERE ID='$dowhat' ORDER BY Bez_DE");
     $XTemp = mysql_fetch_row($MyEntry);
    ?><B>Kategorie bearbeiten:</B><P>
    <form action="?" method="post">
    <input type="hidden" name="do" value="saveme">
    <input type="hidden" name="CngID" value="<? echo $dowhat; ?>">
    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0">
    <TR><TD VALIGN="Middle">Bezeichnung DE: &nbsp;&nbsp;</TD><TD VALIGN="Middle"><INPUT TYPE="text" NAME="bez_de" SIZE="50" MAXLENGTH="50" value="<? echo $XTemp[2]; ?>"></TD></TR>
    <TR><TD VALIGN="Middle">Bezeichnung EN: &nbsp;&nbsp;</TD><TD VALIGN="Middle"><INPUT TYPE="text" NAME="bez_en" SIZE="50" MAXLENGTH="50" value="<? echo $XTemp[3]; ?>"></TD></TR>
    <TR><TD VALIGN="Middle">Bezeichnung FR: &nbsp;&nbsp;</TD><TD VALIGN="Middle"><INPUT TYPE="text" NAME="bez_fr" SIZE="50" MAXLENGTH="50" value="<? echo $XTemp[4]; ?>"></TD></TR>
    <TR><TD VALIGN="Middle"> </TD><TD VALIGN="Middle"><INPUT TYPE="submit" VALUE="Kategorienamen speichern"></TD></TR>
    </TABLE>
    </form><CENTER>
    <?
     Abbruch ();
     echo "</CENTER>";
     }
if ($donow == "saveme")
    {
    mysql_query("UPDATE mods2_rubrik SET Bez_DE='$bez_de', Bez_EN='$bez_en', Bez_FR='$bez_fr' WHERE ID='$CngID' LIMIT 1");
    $donow = "";
    }



if (($donow == "")and($dowhat == ""))                                           # Uebersicht
    {
    ?><B>Neue Kategorie anlegen:</B><br>
    <FONT COLOR="#b00000">Kategorie hier anlegen, dann aber den Ordner (ID Name) per FTP löschen und neu anlegen + chmod 0777..</FONT>  <BR><BR>
    <form action="?" method="post">
    <input type="hidden" name="do" value="addnewrbr">
    <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0">
    <TR><TD VALIGN="Middle">ID: &nbsp;&nbsp;</TD><TD VALIGN="Middle"><INPUT TYPE="text" NAME="CngID" SIZE="20" MAXLENGTH="20">&nbsp; ( = Ordnername, also keine Sonderzeichen, Leerzeichen, Umlaute usw.)</TD></TR>
    <TR><TD VALIGN="Middle">Bezeichnung DE: &nbsp;&nbsp;</TD><TD VALIGN="Middle"><INPUT TYPE="text" NAME="bez_de" SIZE="50" MAXLENGTH="50"></TD></TR>
    <TR><TD VALIGN="Middle">Bezeichnung EN: &nbsp;&nbsp;</TD><TD VALIGN="Middle"><INPUT TYPE="text" NAME="bez_en" SIZE="50" MAXLENGTH="50"></TD></TR>
    <TR><TD VALIGN="Middle">Bezeichnung FR: &nbsp;&nbsp;</TD><TD VALIGN="Middle"><INPUT TYPE="text" NAME="bez_fr" SIZE="50" MAXLENGTH="50"></TD></TR>
    <TR><TD VALIGN="Middle"> </TD><TD VALIGN="Middle"><INPUT TYPE="submit" VALUE="Kategorie anlegen"></TD></TR>
    </TABLE>
    </form>

    <HR SIZE="1"><br><B>Kategorienamen bearbeiten:<P></B>
    <CENTER><TABLE BORDER="0" CELLSPACING="1" CELLPADDING="2" class="outertablebg" WIDTH="440">
    <TR><TD VALIGN="Middle" class="marktabletd"><b>DE</b></TD><TD VALIGN="Middle" class="marktabletd"><b>EN</b></TD><TD VALIGN="Middle" class="marktabletd"><b>FR</b></TD><TD class="marktabletd"> </TD></TR>
    <?
    $GetSql = mysql_query("SELECT * FROM mods2_rubrik ORDER BY Bez_DE");
    while ($datensatz = mysql_fetch_row($GetSql))
          {
       if ($MyCol != "marktabletd")        # Zeilen abwechselnd markieren
           {
           $MyCol = "marktabletd";
           }
           else
               {
               $MyCol = "tabletd";
               }
          echo '<TR>';
          echo '<TD VALIGN="Middle" class="'.$MyCol.'">'.$datensatz[2].'</TD>'."\n";
          echo '<TD VALIGN="Middle" class="'.$MyCol.'">'.$datensatz[3].'</TD>'."\n";
          echo '<TD VALIGN="Middle" class="'.$MyCol.'">'.$datensatz[4].'</TD>'."\n";
          echo '<FORM ACTION METHOD="POST" ACTION="?"><TD VALIGN="Top" ALIGN="Center" class="'.$MyCol.'"><input type="hidden" name="VarID" value="'.$datensatz[1].'"><input type="hidden" name="do" value="editrbrname">&nbsp;&nbsp;<input type="submit" value="edit"></input>&nbsp;&nbsp;</TD></FORM>'."\n";
          echo '</TR>';
          }
    echo '</TABLE>';
    echo "</CENTER>";
    ?>
    <BR> <HR SIZE="1"><br><B>Einträge verschieben / Kategorie auswählen:<P></B>
    <CENTER><TABLE BORDER="0" CELLSPACING="1" CELLPADDING="2" class="outertablebg" WIDTH="250"><?
    $GetSql = mysql_query("SELECT * FROM mods2_rubrik ORDER BY Bez_DE");
    while ($datensatz = mysql_fetch_row($GetSql))
          {
       if ($MyCol != "marktabletd")        # Zeilen abwechselnd markieren
           {
           $MyCol = "marktabletd";
           }
           else
               {
               $MyCol = "tabletd";
               }
          echo '<TR>';
          echo '<TD VALIGN="Middle" class="'.$MyCol.'">'.$datensatz[2].'</TD>'."\n";
          echo '<FORM ACTION METHOD="POST" ACTION="?"><TD VALIGN="Top" ALIGN="Center" class="'.$MyCol.'"><input type="hidden" name="VarID" value="'.$datensatz[1].'">&nbsp;&nbsp;<input type="submit" value="edit"></input>&nbsp;&nbsp;</TD></FORM>'."\n";
          echo '</TR>';
          }
    echo '</TABLE>';
    echo "</CENTER>";
    }


if (($donow == "")and($dowhat != ""))                                           # Eintrage verschieben
    {
    if ($CngID != "")
        {
        $OldDir = "../download/mods/$oldrbr/$Filename.zip";
        $NewDir = "../download/mods/$newrbr/$Filename.zip";
        if (!copy($OldDir, $NewDir))
             {
             print ("<P>[!] failed to copy $Filename.zip...<p>\n");
             }
        if (!unlink($OldDir))
             {
             print ("<P>[!] failed to delete $Filename.zip...<p>\n");
             }
        $OldDir = "../global/grafik/mods/$oldrbr/$Filename.png";
        $NewDir = "../global/grafik/mods/$newrbr/$Filename.png";
        if (!copy($OldDir, $NewDir))
             {
             print ("<P>[!] failed to copy $Filename.png...<p>\n");
             }
        if (!unlink($OldDir))
             {
             print ("<P>[!] failed to delete $Filename.png...<p>\n");
             }
        mysql_query("UPDATE mods2_data SET type='$newrbr' WHERE id='$CngID' LIMIT 1");
        }
    $mn=0;
    $MyRubriken = mysql_query("SELECT ID,Bez_DE FROM mods2_rubrik ORDER BY Bez_DE"); while ($datensatz = mysql_fetch_row($MyRubriken)){$maraay[$mn]=$datensatz[0];$naraay[$mn]=$datensatz[1];$mn ++;}
    $GetSql = mysql_query("SELECT * FROM mods2_data WHERE type='$dowhat'");
    ?><B>Eintrag verschieben:<P></B>
    <CENTER><TABLE BORDER="0" CELLSPACING="1" CELLPADDING="2" class="outertablebg" width="440"><?
    while ($datensatz = mysql_fetch_row($GetSql))
          {
          if ($MyCol != "marktabletd")        # Zeilen abwechselnd markieren
             {
             $MyCol = "marktabletd";
             }
             else
                 {
                 $MyCol = "tabletd";
                 }
          echo '<TR>';
          echo '<TD VALIGN="Middle" class="'.$MyCol.'"><img src="../global/grafik/mods/'.$datensatz[1]."/".$datensatz[5].'.png" width=20 height=20></TD>'."\n";
          echo '<TD VALIGN="Middle" class="'.$MyCol.'">'.$datensatz[2].'</TD>'."\n";
          echo '<TD VALIGN="Middle" class="'.$MyCol.'">'.$datensatz[3].'</TD>'."\n";
          echo '<TD VALIGN="Middle" class="'.$MyCol.'">'.$datensatz[4].'</TD>'."\n";
          echo '<form action="?" method="post">';
          echo '<TD VALIGN="Middle" class="'.$MyCol.'" ALIGN="Center">';
          echo '<input type="hidden" name="CngID" value="'.$datensatz[0].'">';
          echo '<input type="hidden" name="oldrbr" value="'.$datensatz[1].'">';
          echo '<input type="hidden" name="VarID" value="'.$dowhat.'">';
          echo '<input type="hidden" name="Filename" value="'.$datensatz[5].'">';
          echo '<SELECT NAME="newrbr" onChange=submit()>';
          $mn=0;
          foreach ($maraay as $value)
                   {
                   $mysel = "";
                   if ($datensatz[1] == $value) {$mysel = " selected";}
                   echo '<option value='.$value.$mysel.'>'.$naraay[$mn]."\n";
                   $mn ++;
                   }
          echo '</SELECT></TD></form>';
          echo '</TR>';
          }
    echo '</TABLE>';
    Abbruch ();
    echo "</CENTER>";

    }

mysql_close($db);
?>
</TD></TR>
</TABLE>




<?
function Abbruch() {
    echo '<FORM ACTION METHOD="POST" ACTION="?">';
    echo '<input type="hidden" name="VarID" value=""><input type="hidden" name="do" value=""><input type="submit" value="Abbrechen"></input>';
    echo '</FORM>'."\n";
    }