<?php

#########################################################################################
# PHP-Web-Statistik                                                                        #
#                                                                                        #
# Copyright (c) 2006 - PHP-Web-Statistik                                                #
#                                                                                        #
# This file is part of php-web-statistik.                                                #
#                                                                                        #
# Version: 1.5 Release date: Mai 2006                                                        #
# Authors: Reimar Hoven, Holger Naves, Jens Chlum                                        #
# PHP-Web-Statistik official web site and latest version:                                #
#                                                        http://www.php-web-statistik.de #
#########################################################################################

 $site                = strip_tags ( $_GET [ 'site'    ] );
 $jsref               = strip_tags ( $_GET [ 'jsref'   ] );
 $jsres               = strip_tags ( $_GET [ 'jsres'   ] );
 $jscolor             = strip_tags ( $_GET [ 'jscolor' ] );

if($_GET['mode'] == "js")
{
 header("content-type: text/javascript");
 echo "ref='' + unescape(document.referrer);";
 echo "site=\"".$_GET['site'].'";';
 echo "f='' + unescape(document.referrer);";
 echo "\nw=screen.width;\nh=screen.height;\nv=navigator.appName;\n";
 echo "if (v != 'Netscape') {var c=screen.colorDepth;}\nelse {var c=screen.pixelDepth;}\n";
 echo 'jsinfo = "pixel.php?site=" + site +"&jsres=" + w + "x" + h + "&jsref=" + f + "&jscolor=" + c;';
 echo "\n".'str = "<img src = \""+jsinfo+"\" alt=\"\" height=\"1\" width=\"1\">"';
 echo "\n".'document.write(str+"\n");'."\n";
}

if(!$_COOKIE['dontcount'] and !ereg("(b|B)ot", $_SERVER["HTTP_USER_AGENT"]) and !ereg("Yahoo! Slurp", $_SERVER["HTTP_USER_AGENT"]) and !ereg("(s|S)pider", $_SERVER["HTTP_USER_AGENT"]) and $_GET['mode'] != "js")
{
 if(!empty($_GET['site'])) {
  header("Content-Type: image/gif");
  header("Content-Lenght: ".filesize("pixel.gif"));
  readfile("pixel.gif");
 }

 $ret      = time ();                       # unix timestamp
 $ip       = getenv ( "REMOTE_ADDR" );      # get IP address
 $ip_name  = gethostbyaddr ( $ip );         # get hostname
 $database = "../bpstats/log/logdb.dta";               # edit the folder to your logfile

 $accessdb = fopen ( $database , "a+" );
  fwrite ( $accessdb , "\n".$ret."*".$ip."*".$ip_name."*".$_SERVER['HTTP_USER_AGENT']."*".$site."*".str_replace("&", "&amp;", $jsref)."*".$jsres."*".$jscolor."*" );
 fclose ( $accessdb );
}

?>