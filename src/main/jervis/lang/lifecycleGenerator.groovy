package jervis.lang

import jervis.exceptions.UnsupportedLanguageException
import jervis.lang.lifecycleValidator
import jervis.lang.toolchainValidator
import jervis.tools.scmGit
import org.yaml.snakeyaml.Yaml

/**
  WIP: Docs will be written once this class has stabilized.
 */
class lifecycleGenerator {
    def jervis_yaml
    def language
    def lifecycle_obj
    def toolchain_obj
    def lifecycleGenerator() {
        def git = new scmGit()
        this.loadLifecycles("${git.getRoot()}/src/resources/lifecycles.json")
        this.loadToolchains("${git.getRoot()}/src/resources/toolchains.json")
    }
    public void loadLifecycles(String file) {
        this.lifecycle_obj = new lifecycleValidator()
        this.lifecycle_obj.load_JSON(file)
        this.lifecycle_obj.validate()
    }
    public void loadToolchains(String file) {
        this.toolchain_obj = new toolchainValidator()
        this.toolchain_obj.load_JSON(file)
        this.toolchain_obj.validate()
    }
    /**
      Load Jervis YAML to be interpreted.  This YAML will be used to generate the build scripts and components of a Jenkins job.
     */
    public void loadYaml(String raw_yaml) {
        if(!lifecycle_obj) {
        }
        def yaml = new Yaml()
        this.jervis_yaml = yaml.load(raw_yaml)
        this.language = this.jervis_yaml['language']
        if(!lifecycle_obj.supportedLanguage(this.language)) {
            throw new UnsupportedLanguageException(this.language)
        }
    }
    /**
      This will check if the loaded YAML is a matrix build.  The requirements for it
      to be a matrix build is that it must be a matrix specifically for the selected
      language and the array for the section must be greater than 1.

      <p>For example the following YAML would not produce a matrix build.</p>
      <pre><tt>language: groovy
env: foo=bar</tt></pre>
      <pre><tt>language: groovy
env:
  - foo=bar</tt></pre>
      <p>However, the following YAML will produce a matrix build.
      <pre><tt>language: groovy
env:
  - foobar=foo
  - foobar=bar</tt></pre>

      @return <tt>true</tt> if a matrix build will be generated or <tt>false</tt> if it will just be a regular build.
     */
    public Boolean isMatrixBuild() {
        def keys = jervis_yaml.keySet() as String[]
        Boolean result=false
        keys.each{
            if(toolchain_obj.supportedMatrix(language, it)) {
                if(jervis_yaml[it] instanceof ArrayList && jervis_yaml[it].size() > 1) {
                     result=true
                }
            }
        }
        return result
    }
    public String excludeFilter() {
    }
    public String generateToolchainSection() {
    }
    public String generateBeforeInstall() {
    }
    public String generateInstall() {
    }
    public String generateBeforeScript() {
    }
    public String generateScript() {
    }
    public String generateAfterSuccess() {
    }
    public String generateAfterFailure() {
    }
    public String generateAfterScript() {
    }
    public String generateAll() {
    }
}