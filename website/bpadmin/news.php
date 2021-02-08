<?

    # ====================================================== #
    # BP2k6 - Admin -> NewsSystem                            #
    # ------------------------------------------------------ #
    # (c)2006 maus / m.schmitz / maussites.de                #
    # LastChange: 16.04.2006                                 #
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

<CENTER><TABLE BORDER="0" CELLSPACING="1" CELLPADDING="7" class="outertablebg" WIDTH="650" HEIGHT="92%">
<TR><TD VALIGN="Top" HEIGHT="10">
<span class="head"><B>BP2k6</B> -&gt; Admin -&gt; News</B></span>
</TD></TR>
<TR><TD VALIGN="Top" HEIGHT="5" class="marktabletd">
<? require_once ('nav.inc.php'); ?>
</TD></TR>

<TR><TD class="tabletd" VALIGN="Top" HEIGHT="99%">

<?
require_once ("settings.inc.php");
$db=mysql_connect($dbhost,$dbuser,$dbpass);                                     # open db
mysql_select_db($dbname);

if ($DebugMode == "1") { echo "<B>[ GET: ".$_GET["do"]." / POST: ".$_POST["do"]." / id: ".$_POST["VarID"]." ]</B><P>"; }


$donow = $_GET["do"];                ### was machen wir..? :)

if ($donow == "new")                 # Neuer Eintrag? => Speichern
    {

{PictUpload($file,$file_name,$file_size);}
$timenow = date("d.m.Y, H:i:s",time());
$AddNow = "INSERT INTO news (Datum, Head_D, Text_D, Head_E, Text_E, Head_F, Text_F, Autor, Bild )
                       VALUES (
                              '$timenow',
                              '$_POST[head_d]',
                              '$_POST[text_d]',
                              '$_POST[head_e]',
                              '$_POST[text_e]',
                              '$_POST[head_f]',
                              '$_POST[text_f]',
                              '$_POST[autor]',
                              '$file_name'
                              )
                       ";
mysql_query($AddNow);
echo '<HR SIZE="1"><B>Datensatz gespeichert!</B><HR SIZE="1">';
$ProID  = md5 (uniqid (rand()));
echo '<META HTTP-EQUIV="Refresh" CONTENT="1; URL=news.php?'.$ProID.'">';
    }


if ($_POST["do"] == "delete")        # Eintrag loeschen?
    {
    $ClrID = $_POST["VarID"];

    $GetSql = mysql_query("SELECT Bild FROM news WHERE NewsID='$ClrID'");
    $XTemp = mysql_fetch_row($GetSql);
    $ClrPict = $XTemp[0];
    if ($ClrPict != "")
        {
        $OldFile = "../global/grafik/news/".$ClrPict; unlink ($OldFile);
        $OldFile = "../global/grafik/news/pv_".$ClrPict; unlink ($OldFile);
        }

    mysql_query("DELETE FROM news WHERE NewsID='$ClrID' LIMIT 1");
    echo '<HR SIZE="1"><B>Datensatz gel&ouml;scht!</B><HR SIZE="1">';
    $ProID  = md5 (uniqid (rand()));
    echo '<META HTTP-EQUIV="Refresh" CONTENT="1; URL=news.php?'.$ProID.'">';
    }


## ----------------------------------------------------------------------------- News edit
if ($_POST["do"] == "edit") {
?>
<B>News editieren</B>:
<FORM METHOD="Post" ACTION="?do=save" enctype="multipart/form-data">
<input type="hidden" name="SaveID" value="<? echo $_POST["VarID"]; ?>">
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="100%" class="outertablebg">
<? MyForm(); ?>
<TR>
<TD>&nbsp;</TD>
<TD><input type="submit" value="&Auml;nderungen speichern">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="?do=0" class="oncolor"><B>&gt;&gt; [Abbrechen] &lt;&lt;</B></a></TD></TR>
</TABLE>
<HR SIZE="1">
</FORM>
<?
}

## ----------------------------------------------------------------------------- News edit save

if (($_GET["do"] == "save")and($_POST["SaveID"] != "")) {

{PictUpload($file,$file_name,$file_size);}

if ($file_name != "")
    {
    if ($_POST["OrgPict"] != "")
        {
        $OldFile = "../global/grafik/news/".$_POST["OrgPict"]; unlink ($OldFile);
        $OldFile = "../global/grafik/news/pv_".$_POST["OrgPict"]; unlink ($OldFile);
        }
    $AktPictFile = $file_name;
    if ($DebugMode == "1") {echo "Bild ersetzt";}
    }
elseif ($_POST["clrpict"] == "1")
        {
        $AktPictFile = "";
        $OldFile = "../global/grafik/news/".$_POST["OrgPict"]; unlink ($OldFile);
        $OldFile = "../global/grafik/news/pv_".$_POST["OrgPict"]; unlink ($OldFile);
        if ($DebugMode == "1") {echo "Bild gelöscht";}
        }
else {
     $AktPictFile = $_POST["OrgPict"];
     if ($DebugMode == "1") {echo "Bild beibehalten";}
     }

{EditEntry($AktPictFile);}

$ProID  = md5 (uniqid (rand()));
echo '<META HTTP-EQUIV="Refresh" CONTENT="1; URL=news.php?'.$ProID.'">';
}

## ----------------------------------------------------------------------------- News ausgeben (wenn _nicht_ im EDIT Fenster)

if ($_POST["do"] != "edit") {
echo '<B>News:</B><P>';
$GetSql = mysql_query("SELECT * FROM news ORDER BY NewsID desc");

?>
<TABLE BORDER="0" CELLSPACING="1" CELLPADDING="2" class="outertablebg" WIDTH="100%">
<TR>
<TD VALIGN="Top" class="tabletd"><B>ID</B></TD>
<TD VALIGN="Top" class="tabletd"><B>Datum</B></TD>
<TD VALIGN="Top" class="tabletd"><B>Headline</B></TD>
<TD VALIGN="Top" class="tabletd" COLSPAN="3" ALIGN="Center"><B>Sprache</B></TD>
<TD width="1" class="tabletd">&nbsp;</TD>
<TD VALIGN="Top" class="tabletd" COLSPAN="3" ALIGN="Center"><B>Funktion</B></TD>
</TR>

<?
while ($data = mysql_fetch_row($GetSql))
       {
       if ($MyCol != "marktabletd")        # Zeilen abwechselnd markieren
           {
           $MyCol = "marktabletd";
           }
           else
               {
               $MyCol = "tabletd";
               }
       echo '<TR HEIGHT="5"><TD VALIGN="Middle" class="'.$MyCol.'">'.$data[0].'</TD>'."\n";
       $NurDate = explode(",", $data[1]);
       echo '<TD VALIGN="Middle" class="'.$MyCol.'"><NOBR>'.$NurDate[0].'</TD>'."\n";

       if ($data[2] != "")                 # Eine der DREI Headlines wird doch wohl ausgefuellt sein..? :)
           {
           $ThisHeadline = $data[2];
           }
           elseif ($data[4] != "")
                   {
                   $ThisHeadline = $data[4];
                   }
                   else
                       {
                       $ThisHeadline = $data[6];
                       }

       if ($data[9] != "")
           {
           $IsPict ="<B>B</B>";
           }
           else {
                $IsPict = "&nbsp;";
                }
       $MyHeadline = substr ($ThisHeadline,0,70)."..";
       echo '<TD VALIGN="Middle" class="'.$MyCol.'">'.$MyHeadline.'</TD>'."\n";
       $MarkMe = "";
       if ($data[3] != "") {$MarkMe = '<IMG SRC="grfx/flag_d.gif" WIDTH="16" HEIGHT="10">';}
       echo '<TD VALIGN="Middle" ALIGN="Center" class="'.$MyCol.'">'.$MarkMe.'</TD>'."\n";
       $MarkMe = "";
       if ($data[5] != "") {$MarkMe = '<IMG SRC="grfx/flag_e.gif" WIDTH="16" HEIGHT="10">';}
       echo '<TD VALIGN="Middle" ALIGN="Center" class="'.$MyCol.'">'.$MarkMe.'</TD>'."\n";
       $MarkMe = "";
       if ($data[7] != "") {$MarkMe = '<IMG SRC="grfx/flag_f.gif" WIDTH="16" HEIGHT="10">';}
       echo '<TD VALIGN="Middle" ALIGN="Center" class="'.$MyCol.'">'.$MarkMe.'</TD>'."\n";
       echo '<TD width="1" class="'.$MyCol.'">'.$IsPict.'</TD>'."\n";
       echo '<FORM ACTION METHOD="POST" ACTION="news.php"><TD VALIGN="Top" ALIGN="Center" class="'.$MyCol.'"><input type="hidden" name="VarID" value="'.$data[0].'"><input type="hidden" name="do" value="edit"><input type="submit" value="edit" class="EditBtn"></input></TD></FORM>'."\n";
       echo '<TD width="1" class="'.$MyCol.'">&nbsp;</TD>'."\n";
       echo '<FORM ACTION METHOD="POST" ACTION="news.php"><TD VALIGN="Top" ALIGN="Center" class="'.$MyCol.'"><input type="hidden" name="VarID" value="'.$data[0].'"><input type="hidden" name="do" value="delete"><input type="submit" value="DEL" class="DelBtn" onClick="return confirm(\'[ACHTUNG] Soll der Eintrag\n\n '.$MyHeadline.'\n\nunwiderruflich gelöscht werden ?\')"></input></TD></FORM></TR>'."\n";
       }

echo '</TABLE>';

## ----------------------------------------------------------------------------- News eingeben
?>
<BR><HR SIZE="1">

<FORM METHOD="Post" ACTION="?do=new" enctype="multipart/form-data">
<B>Neue News anlegen:<P>Autor:
<input type="text" name="autor" size="28" maxlength="28">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<B>Bild (jpg!) zufügen:</B> <input type=file name=file>
<P>
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="2" WIDTH="100%" class="outertablebg">
<? MyForm(); ?>
<TR>
<TD>&nbsp;</TD>
<!--<TD><input type="submit" value="News eintragen!"></TD></TR>        -->
<TD><button name="sende" type="submit">News eintragen!</button></TD></TR>
</TABLE>
<HR SIZE="1">
</FORM>

<?

}
mysql_close($db);                                                               # close DB
?>

</TD></TR>
</TABLE></CENTER>
</BODY>
</HTML>



<?
## ----------------------------------------------------------------------------- Funktionen


#______________________________________________________________________  ####### News Aendern

function EditEntry($AktPictFile) {
$EditID = $_POST["SaveID"];
$SaveEdit = "UPDATE news SET Head_D='$_POST[head_d]', Text_D='$_POST[text_d]', Head_E='$_POST[head_e]', Text_E='$_POST[text_e]', Head_F='$_POST[head_f]', Text_F='$_POST[text_f]', Bild='$AktPictFile'  WHERE NewsID='$EditID' LIMIT 1";
mysql_query($SaveEdit);
echo '<HR SIZE="1"><B>Datensatz ge&auml;ndert!</B><HR SIZE="1">';
}


#______________________________________________________________________  ####### Bild hochladen / Thumbnail

function PictUpload($file,$file_name,$file_size) {

############## Config:
$SaveQuality = "80";  # bigger = besser :)
$ResizeBy = "h";      # h=horizontal, v=vertikal, a=auto / ReSize nach groesserer Kantenlaenge
$NewSize = "75";     # neue groesse
$SaveDir = "../global/grafik/news";    # in welches Verzeichnis speichern
$ThumbPref = "pv_";   # Prefix fuer Thumbs
$CheckSize = "yes";   # Dateigroesse pruefen? (yes/no)
$MaxSize = "100000";   # bytes

############## Datei upload
#if ($file_name == "") {die("<br><br>Keine Datei gewählt.");}
#if (file_exists("$SaveDir/$file_name")) {die("<br><br>Datei ist bereits vorhanden.");}
if ($file_name != "") {
if ($CheckSize == "yes") {
    if ($file_size > $MaxSize)
        {
        die("<br><br>Die Datei ist zu groß !");
        }
    }
$upload = "$file_name";
@copy($file, "$SaveDir/$upload") or die("<br><br>Datei konnte nicht hochgeladen werden!");

############## Thumbnail erzeugen
$scr = imagecreatefromjpeg("$SaveDir/$upload");
$width = imagesx($scr);
$height = imagesy($scr);

if ($ResizeBy == "h")     ## wonach skalieren wir..?
    {
    $Scale = $width / $NewSize;
    $x = round($width / $Scale);
    $y = round($height / $Scale);
    }
 elseif ($ResizeBy == "v")
    {
    $Scale = $height / $NewSize;
    $x = round($width / $Scale);
    $y = round($height / $Scale);
    }
  else
    {
    if ($width > $height) {$Scale = $width / $NewSize;}   # ungetestet !
        else {$Scale = $height / $NewSize;}
    $x = round($width / $Scale);
    $y = round($height / $Scale);
    }

$dst = imagecreatetruecolor($x,$y);
imagecopyresampled($dst,$scr,0,0,0,0,$x,$y,$width,$height);
imagejpeg($dst, "$SaveDir/".$ThumbPref.$upload, $SaveQuality);
}

}

#______________________________________________________________________ ####### Formular ausgeben

function MyForm() {
$EditID = $_POST["VarID"];
if ($_POST["do"] == "edit")
    {
    $GetSql = mysql_query("SELECT * FROM news WHERE NewsID='$EditID'");
    $XOrg = mysql_fetch_row($GetSql);

               echo '<TR><TD COLSPAN="2" class="tabletd">';
               echo '<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0"><TR>';
               if ($XOrg[9] != "")
                   {
                   echo '<TD VALIGN="Top"><img src="../global/grafik/news/pv_'.$XOrg[9].'" border="1"></TD>';
                   echo '<TD>&nbsp;&nbsp;&nbsp;&nbsp;</TD>';
                   }
               echo '<TD VALIGN="Top"';
               echo '<B>Neues Bild hochladen:</B> <input type=file name=file><P>';
               if ($XOrg[9] != "")
                   {
               echo '<B>Bild vom Server löschen:</B> <INPUT TYPE="radio" NAME="clrpict" VALUE="1"> (und keins anzeigen) ';
                    }
               echo '</TD>';
               echo '</TR></TABLE>';
               echo '</TD></TR>';
               }

?>
<INPUT TYPE="hidden" NAME="OrgPict" VALUE="<? echo $XOrg[9]; ?>">
<TR>
<TD COLSPAN="2"><IMG SRC="grfx/flag_d.gif">&nbsp;&nbsp;&nbsp;<span class="head"><B>Deutsch</B></span></TD></TR>
<TR>
<TD VALIGN="Middle" class="tabletd">Überschrift:</TD>
<TD VALIGN="Middle" class="tabletd"><input type="text" name="head_d" size="80" maxlength="125" value="<? echo $XOrg[2]; ?>"></TD></TR>
<TR>
<TD VALIGN="Top" class="tabletd">Text:</TD>
<TD VALIGN="Top" class="tabletd"><textarea name="text_d" rows="6" cols="82"><? echo $XOrg[3]; ?></textarea></TD></TR>

<TR>
<TD COLSPAN="2"><IMG SRC="grfx/flag_e.gif">&nbsp;&nbsp;&nbsp;<span class="head"><B>English</B></span>&nbsp;</TD></TR>
<TR>
<TD VALIGN="Middle" class="tabletd">Überschrift:</TD>
<TD VALIGN="Middle" class="tabletd"><input type="text" name="head_e" size="80" maxlength="125" value="<? echo $XOrg[4]; ?>"></TD></TR>
<TR>
<TD VALIGN="Top" class="tabletd">Text:</TD>
<TD VALIGN="Top" class="tabletd"><textarea name="text_e" rows="6" cols="82"><? echo $XOrg[5]; ?></textarea></TD></TR>

<TR>
<TD COLSPAN="2"><IMG SRC="grfx/flag_f.gif">&nbsp;&nbsp;&nbsp;<span class="head"><B>Fran&ccedil;ais</B></span>&nbsp;</TD></TR>
<TR>
<TD VALIGN="Middle" class="tabletd">Überschrift:</TD>
<TD VALIGN="Middle" class="tabletd"><input type="text" name="head_f" size="80" maxlength="125" value="<? echo $XOrg[6]; ?>"></TD></TR>
<TR>
<TD VALIGN="Top" class="tabletd">Text:</TD>
<TD VALIGN="Top" class="tabletd"><textarea name="text_f" rows="6" cols="82"><? echo $XOrg[7]; ?></textarea></TD></TR>
<?
}
?>