<?

    # ====================================================== #
    # BP2k6 - Homepage, HEAD                                 #
    # ------------------------------------------------------ #
    # (c)2006 maus / m.schmitz / maussites.de                #
    # LastChange: 01.06.2006                                 #
    # ====================================================== #


# status / todo:  o.k.
# ______________________________________________________________________________

if ($ThisLanguage == 2)
   {$ScreenText = "This screenshots are taken from the German version.<BR>Of course everything is translated into English in Slam Soccer 2006";
   $WebTitle = "Xenoage: Slam Soccer 2006 / the funny football game in 3D-comic-style - and it&#180;s for free";
   }
   elseif ($ThisLanguage == 3)
          {$ScreenText = "Ces copies d\'&eacute;cran montrent la version allemande.<BR>Dans la version fran&ccedil;aise, tout est traduit en fran&ccedil;ais bien s&ucirc;r! ";
          $WebTitle = "Xenoage: Coup de Foot 2006 est un jeu comique &agrave; 3D qui est gratuit. Gratuit et open-source.";
          }
          else
          {$ScreenText = " <BR>[ Screenshot Bolzplatz 2006 ]";
          $WebTitle = "Xenoage: Bolzplatz 2006 / das spa�ige Fu�ballspiel im 3D-Comic-Stil f�r lau";
          }

?>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 3.2//EN">
<HTML>
<HEAD>
<link rel=stylesheet type=text/css href=../global/site.css>
<link rel="SHORTCUT ICON" href="../favicon.ico">
<link rel="icon" href="../favicon.ico" type="image/ico">
<title><? echo $WebTitle; ?></title>

<meta name="keywords" content="Fu�ball, Weltmeisterschaft, Open-Source, kostenlos, 3D, Comic, Bolzen, Stadion, Teams, Editor, download, free, Bolzplatz, Slam Soccer, Coup de Foot,  3d-comic-style, stadium, Football, Soccer, Coupe du Monde">
<meta name="description" content="Bolzplatz 2006 ist ein spa�iges Fu�ballspiel im 3D-Comic-Stil f�r lau">
<meta name="author" content="Andreas Wenger, Xenoage / Webdesign M.Schmitz, hauptcomputer.de">
<meta name="publisher" content="Xenoage Software, Andreas Wenger">
<meta name="company" content="Xenoage Software, Andreas Wenger">

<SCRIPT LANGUAGE="JavaScript">
function showPicture(pictureToShow, windowTitle)
{
PicWin=window.open("","PictureViewer",'status=0,scrollbars=no,resizable=yes,width=440,height=341,screenX=50,screenY=50,top=250,left=250');
PicWin.document.open();
PicWin.document.writeln('<html><head><title>Xenoage / Bolzplatz 2006</title></head>');
PicWin.document.writeln('<body topmargin="0" onload="this.focus()" bgcolor="white" leftmargin="0" text="#eeeeee" link="#808080" alink="#808080" vlink="#808080" marginwidth="0" marginheight="0" background="../global/grafik/loading.gif"><table border=0 cellspacing=0 cellpadding=0><TR><TD>');
PicWin.document.writeln('<a href="javascript:this.close()"><img onLoad="window.opener.resizeWindow(10,105,0);" border="1" src="'+ pictureToShow +'"></a>');
PicWin.document.writeln('<CENTER><font face=arial size=1>&nbsp;&nbsp;<? echo $ScreenText; ?></font></CENTER></TD></TR></TABLE></body></html>');
PicWin.document.close();
}
function resizeWindow(Hborder, Vborder, millisecs)
{
setTimeout("PicWin.resizeTo(PicWin.document.images[0].width + " + Hborder + ", PicWin.document.images[0].height + " + Vborder + ");", millisecs);
}
function showPicture2(pictureToShow, windowTitle)
{
PicWin=window.open("","PictureViewer",'status=0,scrollbars=no,resizable=yes,width=440,height=341,screenX=50,screenY=50,top=250,left=250');
PicWin.document.open();
PicWin.document.writeln('<html><head><title><? echo $WebTitle; ?></title></head>');
PicWin.document.writeln('<body topmargin="0" onload="this.focus()" bgcolor="white" leftmargin="0" text="#eeeeee" link="#808080" alink="#808080" vlink="#808080" marginwidth="0" marginheight="0" background="../global/grafik/loading2.gif">');
PicWin.document.writeln('<a href="javascript:this.close()"><img onLoad="window.opener.resizeWindow(10,65,0);" border="1" src="'+ pictureToShow +'"></a>');
PicWin.document.writeln('</body></html>');
PicWin.document.close();
}
function resizeWindow(Hborder, Vborder, millisecs)
{
setTimeout("PicWin.resizeTo(PicWin.document.images[0].width + " + Hborder + ", PicWin.document.images[0].height + " + Vborder + ");", millisecs);
}
</SCRIPT>

</HEAD>
<BODY BACKGROUND="../global/grafik/bg.jpg" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" link="#000000" alink="#000000" vlink="#000000">

<CENTER>
<map name="Map">
  <area shape="rect" coords="600,4,627,20" href="../fr/index.php">
  <area shape="rect" coords="571,4,598,20" href="../en/index.php">
  <area shape="rect" coords="542,4,569,20" href="../de/index.php">
</map>


<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="790" HEIGHT="100%">    <!-- main -->
<TR>

<TD width="10" BACKGROUND="../global/grafik/bg_l.jpg"><IMG SRC="../global/grafik/x.gif" WIDTH="10" HEIGHT="1"></TD>

<TD VALIGN="Top" WIDTH="770" BACKGROUND="../global/grafik/bg_content.jpg">

<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="770" HEIGHT="100%">    <!-- head -->
<TR>
<TD VALIGN="Top" HEIGHT="109"><IMG SRC="<? echo $PreDir; ?>grafik/head.jpg" WIDTH="770" HEIGHT="109"></TD>
</TR><TR>
<TD VALIGN="Top" HEIGHT="54"><IMG SRC="<? echo $PreDir; ?>grafik/head_nav.gif" WIDTH="770" HEIGHT="54" usemap="#Map" border="0"></TD>
</TR><TR>
<TD VALIGN="Top" HEIGHT="99%">
                                                                                <!-- content -->
<TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="770" HEIGHT="96%">
<TR HEIGHT="98%">
<TD VALIGN="Top">

  <TABLE BORDER="0" CELLSPACING="0" CELLPADDING="0" WIDTH="170" HEIGHT="85%">   <!-- navigation -->
  <TR>
  <TD VALIGN="Top" WIDTH="161" BACKGROUND="../global/grafik/bg_nav.jpg">

  <? include ('../global/_navigation.inc.php'); ?>
  </TD>
  <TD VALIGN="Top" WIDTH="9" CLASS="navout"><IMG SRC="../global/grafik/x.gif" WIDTH="9" HEIGHT="1"></TD>
  </TR>
  <TR>
  <TD COLSPAN="2" HEIGHT="10" CLASS="navout"><IMG SRC="../global/grafik/x.gif" WIDTH="170" HEIGHT="10"></TD>
  </TR>
  </TABLE>                                                                      <!-- navigation end -->
</TD>
<TD width="20"><IMG SRC="../global/grafik/x.gif" WIDTH="20" HEIGHT="1"></TD>
<TD width="560" VALIGN="Top">

<!-- CONTENT START -------------------------------------------------------------###### -->