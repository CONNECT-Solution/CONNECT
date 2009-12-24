#!/bin/perl -w

#Transverse through the base project directory and all subdirectories searching for   
#files that contain specific path references in the windows form.  
#The reference follows a form of:  C:\\
#This path will be modified to the solaris form with a provided home directory

use strict;
use Cwd;
use File::Spec::Functions;

if ($#ARGV != 0) {
  # 0 position in input array @ARGV should contain the Solaris home directory 
   print "\n";
   print "Usage: convert_win2sun.pl baseDir\n";
   print "       where baseDir     is the Solaris base directory\n";  
   print "                         (i.e. C:\\ will be replaced with baseDir)\n";
   print "                         (i.e. baseDir is like /usr/local/ )\n";
   print "\n";
} else {
   # Given File is searched to contain a path indicator
   # if found then replacement is made with user confirmation
   sub find_replace_indicator {
    my ($target)=@_;
    my $decision="n";

    if(open(F, "+<$target")) {
      while(<F>) {
        if(m/C:\\/) {
          print "$target contains path indicators \n";

          #print "Replace C:\\ with $ARGV[0] in this file? \"y\" or \"n\" \n";
          $decision="y";
          chomp $decision;
          last;
        }
      }
      if($decision eq "y") {
        seek(F,0,0);
        my @lines = <F>;
        for(my $idx=0; $idx<@lines; $idx++) {
          if($lines[$idx] =~ m/C:\\\\/) {
            #print "$lines[$idx] \n";
            $lines[$idx] =~ s/C:\\\\/$ARGV[0]/g;
            $lines[$idx] =~ s/\\\\/\//g;
            #print "$lines[$idx] \n\n";
          } elsif ($lines[$idx] =~ m/C:\\/) {
            #print "$lines[$idx] \n";
            $lines[$idx] =~ s/C:\\/$ARGV[0]/g;
            $lines[$idx] =~ s/\\/\//g;
            $lines[$idx] =~ s/\/\//\//g;
            #print "$lines[$idx] \n\n";
          }
        }
        seek(F,0,0);
        print F @lines;
      }
    }
    close(F);
  }

   # Given File is searched to contain "netbeans" text
   # if found then replacement is made with user confirmation
  sub find_replace_netbeans {
    my ($target)=@_;
    my $decision="n";

    if(open(F, "+<$target")) {
      while(<F>) {
        if(m/netbeans/i) {
          if(! m/netbeans/) {
            print "$target contains mixed case Netbeans\n";
            #print "Replace occurances with \"netbeans\" in this file? \"y\" or \"n\" \n";
            $decision="y";
            chomp $decision;
            last;
          }
        }
      }
      if($decision eq "y") {
        seek(F,0,0);
        my @lines = <F>;
        for(my $idx=0; $idx<@lines; $idx++) {
          if($lines[$idx] =~ m/netbeans/i) {
            #print "$lines[$idx] \n";
            $lines[$idx] =~ s/netbeans/netbeans/gi;
            #print "$lines[$idx] \n\n";
          }
        }
        seek(F,0,0);
        print F @lines;
      }
    }
    close(F);
  }

  # All files from the given directory are searched
  sub searchdir {
    my ($dir)=@_;
    my $file;
     
    #print "Directory $dir \n";
    opendir(DIR, $dir) || die "Cannot open $dir - $1";
    my @files=grep(!/^\.\.?$/, readdir DIR);
    closedir(DIR);
   
    foreach $file(@files) {
      #my $fullfile="$dir/$file";
	   if ($dir !~ /.svn/){
			my $fullfile = catfile($dir, $file);
			#print "Search file $fullfile \n";
			if (-f $fullfile) {
			   find_replace_indicator($fullfile);
			   find_replace_netbeans($fullfile);
			} else {
			   if (-d $fullfile) {
				 searchdir("$fullfile");
			   }
			}
	   }
    }
  }

  # Start the recursive search in the base project directory
  #searchdir("C:\\projects\\nhinc\\Current\\Product\\Production");

  #temp test
  searchdir(getcwd);
}
