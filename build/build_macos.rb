#!/usr/bin/ruby

##########################################################################
# Copyright (C) 2009 - 2011, Duncan Bayne & Armin Sadeghi
#
# This program is free software; you can redistribute it and/or modify it
# under the terms of version 3 of the GNU Lesser General Public License 
# as published by the Free Software Foundation.
#
# This program is distributed in the hope that it will be
# useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
# Public License for more details.
#
# The GNU Lesser General Public License can be viewed at
# http://www.gnu.org/licenses/lgpl-3.0.txt
#
# To find out more about ObzVault, visit
# https://github.com/asadeghi/obzvault
##########################################################################

def safe_rmdir(path)
  if (File.exists?(path))
    safe_system("rm -rf #{path}")
  end
end

def safe_system(command)
  if (!system(command))
    raise "FAILED!"
  end
end

def build_app(is_trial, output_path, version)
  app_name = "OBZVault#{is_trial ? 'Trial' : ''}"
  app_path = "#{app_name}.app"

  safe_rmdir(app_path)
  safe_system("cp -R macos_files/original #{app_path}")

  # copy in all jars
  safe_system("cp #{output_path}/obzvault.jar #{app_path}/Contents/Resources/Java")
  safe_system("cp #{output_path}/lib/*.jar #{app_path}/Contents/Resources/Java/lib")

  # don't use BSD "chmod -R"
  safe_system("chmod a+rx #{app_path}/Contents/Resources/Java/*.jar")
  safe_system("chmod a+rx #{app_path}/Contents/Resources/Java/lib/*.jar")

  # stamp and name files - can't do in-place with 'sed -i' because MacOS uses BSD sed, argh
  safe_system("sed \"s/\\[VERSION\\]/#{version}/g\" #{app_path}/Contents/Info.plist > Info.plist.tmp")
  safe_system("sed \"s/\\[NAME\\]/#{app_name}/g\" Info.plist.tmp > #{app_path}/Contents/Info.plist")
  safe_system("rm Info.plist.tmp")

  # clean out unwanted files from SVN and Finder; explicitly ignore failure here
  system("find #{app_path} -name '*.svn' -exec rm -rf {} \\;")
  system("find #{app_path} -name '.DS_Store' -exec rm -rf {} \\;")
end

def build_dmg(dmg_original_path, dmg_new_path, dmg_name, app_path)

  dmg_volume_path = "/Volumes/#{dmg_name}"

  safe_system("cp #{dmg_original_path} #{dmg_new_path}")
  safe_system("hdiutil attach #{dmg_new_path} -mountpoint #{dmg_volume_path}")
  
  # clean out unwanted files from SVN but _not_ Finder; explicitly ignore failure here
  system("sudo find #{dmg_volume_path} -name '*.svn' -exec rm -rf {} \\;")

  safe_system("cp -Rf #{app_path} #{dmg_volume_path}")
  safe_system("hdiutil eject #{dmg_volume_path}")
end

def get_version()
  version = nil
  open("version.txt", "r") { | f | 
    version = f.gets().chop()
  }  
  if (version == nil) 
    raise "Could not read version.txt"
  end

  return "#{version}"
end

def show_usage
  puts "usage: build_macos.rb OUTPUTDIR"
  puts
  puts "e.g. build_macos.rb /Volumes/BUILD/vault"
end

if (ARGV.length != 1 or ARGV[0].length <= 3)
  show_usage()
else
  output_dir = ARGV[0]
  version = get_version()

  build_app(true, "#{output_dir}/trial", version)
  build_dmg("macos_files/OBZVaultTrial.dmg", "OBZVaultTrial-#{version}.dmg", "OBZVaultTrial", "OBZVaultTrial.app")

  build_app(false, "#{output_dir}/release", version)
  build_dmg("macos_files/OBZVault.dmg", "OBZVault-#{version}.dmg", "OBZVaultTrial", "OBZVault.app")

  safe_system("rm -rf *.app")
  safe_system("mv *.dmg #{output_dir}")
end
