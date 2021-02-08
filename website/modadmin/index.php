<?

    # ====================================================== #
    # BP2k6 - Admin -> AddMods                               #
    # ------------------------------------------------------ #
    # (c)2006 maus / m.schmitz / maussites.de                #
    # LastChange: 22.06.2006                                 #
    # ====================================================== #


# status / todo:  o.k.
# ______________________________________________________________________________

$MyDir = $_POST[type];

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
<span class="head"><B>BP2k6</B> -&gt; Admin -&gt; Mods</B></span>
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
    echo '<B>Neuen Mod eintragen</B>:';
    $fatal = 0;


    $NewPict = explode(".", $modpict_name);
    $NewFile = explode(".", $modfile_name);
    if ($NewPict[0] != $NewFile[0])                       # BILD- und ZIP-Name muessen gleich sein!
        {
        $fatal = 1;
        echo "<BR><BR><BR><HR SIZE=1><B>FEHLER:</B> Bild-Name und ZIP-Name sind nicht gleich!<P><B>[!]</B> Mod wurde nicht angelegt.";
        echo '<P><a href = "javascript:history.back()"><b>&lt;&lt; Zur&uuml;ck</b></a><HR SIZE=1>';
        }
    if ($modpict_name == "" or $modfile_name == "")       # Wurden Bild/Zip angegeben?
        {
        $fatal = 1;
        echo "<BR><BR><BR><HR SIZE=1><B>FEHLER:</B> Bild oder ZIP nicht angegeben!<P><B>[!]</B> Mod wurde nicht angelegt.";
        echo '<P><a href = "javascript:history.back()"><b>&lt;&lt; Zur&uuml;ck</b></a><HR SIZE=1>';
        }
    if ($NewFile[1] != "zip")                             # Ist Dateiendung des zip = zip..? :)
        {
        $fatal = 1;
        echo "<BR><BR><BR><HR SIZE=1><B>FEHLER:</B> Das File ist kein ZIP!<P><B>[!]</B> Mod wurde nicht angelegt.";
        echo '<P><a href = "javascript:history.back()"><b>&lt;&lt; Zur&uuml;ck</b></a><HR SIZE=1>';
        }


   if ($fatal == 0)                                       # ..dann lade Bild hoch (egal welches Format)
        {
        $add="../global/grafik/mods/$MyDir/$modpict_name";
        if(move_uploaded_file ($modpict, $add))
           {
           echo "<P>Bild gespeichert..";
           }
           else {$fatal = 1;}
        }
   if ($fatal == 0)
        {
        $add="../download/mods/$MyDir/$modfile_name";     # ..und lade zip hoch
        if(move_uploaded_file ($modfile, $add))
           {
           echo "<br>zip gespeichert..";
           }
           else {$fatal = 1;}
        }
   if ($fatal == 0)                                       # ..und DB Eintrag
       {
       $file_name = $NewFile[0];
       $AddNow = "INSERT INTO mods2_data (type, name, author, date, file )
                       VALUES (
                              '$_POST[type]',
                              '$_POST[name]',
                              '$_POST[author]',
                              '$_POST[date]',
                              '$file_name'
                              )
                       ";
       mysql_query($AddNow);
       echo "<br>Datenbankeintrag gespeichert..";         # und fertig :)
       ?>
       <P><BR><a href="index.php"><b>[weitere Mods eintragen]</b></a>
       <?
       }
       else {
            echo "<BR><BR><BR><HR SIZE=1><B>FEHLER:</B> Dateien konnten nicht geschrieben werden!<P><B>[!]</B> Mod wurde nicht angelegt.";
            echo '<P><a href = "javascript:history.back()"><b>&lt;&lt; Zur&uuml;ck</b></a><HR SIZE=1>';
            }


    }


else {
?>
<B>Neuen Mod eintragen</B>:<P>

<FORM METHOD="Post" ACTION="?do=new" enctype="multipart/form-data">
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="6">
<TR>
<TD><b>Type:</b></TD>
<TD>
<?
$GetSql = mysql_query("SELECT * FROM mods2_rubrik ORDER BY Bez_DE");
echo '<SELECT NAME="type">';
while ($datensatz = mysql_fetch_row($GetSql))
       {
       echo '<option value='.$datensatz[1].'>'.$datensatz[2]."\n";
       }
?>
</select></TD>
</TR>
<TR>
<TD><b>Name:</b></TD>
<TD><input type="text" name="name" size="55" maxlength="60"></TD>
</TR>
<TR>
<TD><b>Autor:</b></TD>
<TD><input type="text" name="author" size="30" maxlength="60"></TD>
</TR>
<TR>
<TD><b>Datum:</b></TD>
<TD><input type="text" name="date" size="10" maxlength="10" value="<? echo date('Y-m-d'); ?>"></TD>
</TR>
<TR>
<TD><b>Bild:</b></TD>
<TD><input name="modpict" type="file" size="33"></TD>
</TR>
<TR>
<TD><b>File/zip:</b></TD>
<TD><input name="modfile" type="file" size="33"></TD>
</TR>
<TR>
<TD> </TD>
<TD><INPUT TYPE="submit" VALUE="Mod zufügen"></TD>
</TR>
</TABLE>
</FORM>
<?
}



mysql_close($db);
?>
</TD></TR>
</TABLE>