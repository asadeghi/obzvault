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
    safe_system("sudo rm -rf #{path}")
  end
end

def safe_cleandir(path)
  safe_rmdir(path)
  safe_system("mkdir #{path}")
end

def safe_system(command)
  if (!system(command))
    raise "FAILED!"
  end
end

def compile(is_trial, output_path, version)
  app_name = "OBZVault#{is_trial ? ' Trial' : ''}"

  if (is_trial)    
    dwIsTrialFlag = 8088
  else
    dwIsTrialFlag = 6502
  end

  puts("building #{app_name}");
  safe_system("sed -i \"s/Application\.version = .*\..*/Application\.version = #{version}/g\" ../project/src/obzvault/resources/OBZVaultApp.properties")
  safe_system("sed -i \"s/Application\.name = .*/Application\.name = #{app_name}/g\" ../project/src/obzvault/resources/OBZVaultApp.properties")
  safe_system("sed -i \"s/Application\.title = .*/Application\.title = #{app_name}/g\" ../project/src/obzvault/resources/OBZVaultApp.properties")
  safe_system("sed -i \"s/Application\.id = .*/Application\.id = #{app_name}/g\" ../project/src/obzvault/resources/OBZVaultApp.properties")
  safe_system("sed -i \"s/private Float _dwIsTrialFlag = ....f;/private Float _dwIsTrialFlag = #{dwIsTrialFlag}f;/g\" ../project/src/obzvault/OBZVaultDocument.java")
  safe_system("sed -i \"s/assertEquals(....f, _docTest.getTrialStatus(), 0.01f);/assertEquals(#{dwIsTrialFlag}f, _docTest.getTrialStatus(), 0.01f);/g\" ../project/test/obzvault/OBZVaultDocumentTest.java")
  safe_system("ant clean test jar -buildfile ../project/build.xml")
  safe_system("svn revert ../project/src/obzvault/resources/OBZVaultApp.properties")
  safe_system("cp -R ../project/dist #{output_path}")
end

def build_deb(is_trial, output_path, version)
  pkg_name = "obzvault#{is_trial ? 'trial' : ''}"
  deb_path = "#{pkg_name}-#{version}"
  files_path = "debian_files/#{is_trial ? 'trial' : 'release'}"

  safe_rmdir(deb_path)
  safe_system("mkdir #{deb_path}")
  safe_system("mkdir #{deb_path}/debian")
  safe_system("mkdir #{deb_path}/debian/input")

  # files that control
  safe_system("cp debian_files/rules #{deb_path}/rules")
  safe_system("cp #{files_path}/changelog #{files_path}/compat #{files_path}/control #{files_path}/dirs #{files_path}/install #{files_path}/obzvault.desktop #{files_path}/vault.1 #{files_path}/postinst #{files_path}/postrm #{files_path}/obzvault.sharedmimeinfo #{deb_path}/debian")
  safe_system("sed -i \"s/VERSION/#{version}/g\" #{deb_path}/debian/changelog")

  # files that will be installed
  safe_system("cp #{files_path}/vault #{files_path}/copyright #{deb_path}/debian/input")
  safe_system("cp ../project/src/obzvault/resources/app.png #{deb_path}/debian/input")
  safe_system("cp ../project/src/obzvault/resources/doc.png #{deb_path}/debian/input")
  safe_system("cp ../project/src/obzvault/resources/doc.png #{deb_path}/debian/input/application-offbyzero.obzvault.png")
  safe_system("cp ../project/dist/obzvault.jar #{deb_path}/debian/input")
  safe_system("cp ../project/dist/lib/*.jar #{deb_path}/debian/input")

  safe_system("cd #{deb_path};sudo ./rules")
  safe_rmdir(deb_path)
end

def get_version()
  safe_system("svn update")
  revision = nil
  IO.foreach("|svn info .") { |x| 
    if (x =~ /Revision: (\d+)/)
      revision = $1.to_i    
    end
  }
  if revision == nil
    raise "Could not determine revision"
  end

  version = nil
  open("version.txt", "r") { | f | 
    version = f.gets().chop()
    f.close()
  }  
  if (version == nil) 
    raise "Could not read version.txt"
  end

  return "#{version}.#{revision}"
end

def show_usage
  puts "usage: build_linux.rb OUTPUTDIR"
  puts
  puts "e.g. build_linux.rb /mnt/build/vault"
  puts
  puts "NOTE: build_linux.rb will clean all contents from OUTPUTDIR!"
  puts "===="
  puts
end

if (ARGV.length != 1 or ARGV[0].length <= 3)
  show_usage()
else
  output_dir = ARGV[0]

  safe_cleandir("builds")
  safe_cleandir(output_dir)

  version = get_version()

  compile(true, "builds/trial", version)
  build_deb(true, "builds/trial", version)

  compile(false, "builds/release", version)
  build_deb(false, "builds/release", version)

  safe_system("cp -R builds/* #{output_dir}")
  safe_system("mv *.deb #{output_dir}")
end
