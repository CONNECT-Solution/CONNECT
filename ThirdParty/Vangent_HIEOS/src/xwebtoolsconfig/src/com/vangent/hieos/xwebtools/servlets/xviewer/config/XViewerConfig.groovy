/*
 * This code is subject to the HIEOS License, Version 1.0
 *
 * Copyright(c) 2008-2009 Vangent, Inc.  All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vangent.hieos.xwebtools.servlets.xviewer.config

class XViewerConfig {
	def config
	
	XViewerConfig() {
		
        //URL configUrl = this.getClass().getClassLoader().getResource('config.xml')
        URL configUrl = this.getClass().getClassLoader().getResource("com/vangent/hieos/xwebtools/servlets/xviewer/config/config.xml")
		println "configUrl is ${configUrl}"
		config = new XmlSlurper().parse(configUrl.toExternalForm())
	}
	
	// used for unit tests
	XViewerConfig(string) {
		config = new XmlSlurper().parseText(string)
	}
	
	ArrayList<String> getRegistryNames() {
		def names = config.Registry.collect { registry -> registry["@name"] }
		ArrayList<String> jnames = new ArrayList<String>()
		for (String name : names)
			jnames.add(name.toString())
		return jnames
	}
	
	def findRegistry(String registryName) {
		return config.Registry.find { registry -> registry["@name"] == registryName }
	}
	
	String getRegistryAtt(String registryName, String attName) {
		def registry = findRegistry(registryName) 
		String attValue = registry."${attName}"
		return attValue.trim()
	}
		
	ArrayList<String> getRepositoryNames() {
		def names = config.Repository.collect { repository -> repository["@name"] }
		ArrayList<String> jnames = new ArrayList<String>()
		for (String name : names)
			jnames.add(name.toString())
		return jnames
	}
	
	def findRepository(String repositoryName) {
		return config.Repository.find { repository -> repository["@name"] == repositoryName }
	}
	
	String getRepositoryAtt(String repositoryName, String attName) {
		def repository = findRepository(repositoryName) 
		String attValue = repository."${attName}"
		return attValue.trim()
	}
	
	String getConfigProperty(String attName) {
		String value = config."${attName}"
		return value.trim()
	}
	
	def findRg(String rgName) {
		return config.Rg.find { rg -> rg["@name"] == rgName }
	}

	String getRgAtt(String rgName, String attName) {
		def rg = findRg(rgName) 
		String attValue = rg."${attName}"
		return attValue.trim()
	}
	
	ArrayList<String> getRgNames() {
		def names = config.Rg.collect { rg -> rg["@name"] }
		ArrayList<String> jnames = new ArrayList<String>()
		for (String name : names)
			jnames.add(name.toString())
		return jnames
	}
	
	boolean isRg(String name) {
		return getRgNames().any { it == name }
	}
	
	boolean isRegistry(String name) {
		return getRegistryNames().any { it == name }
	}
	

}
