package spock.lair.fileio

import spock.lair.editor.Editor.log
import spock.lair.strings.key
import spock.lair.strings.type
import java.io.File

/*

route:../src/sandbox
  ./site/
      /.html
     /local/md/(content)(component)/.md
    /public/(css/)(js/)(assets/)
 (assets)/img
site: /contadeal
pages: /pages
assets//img//font
*/



object Storage {
    val site = "contadeal"
    fun cli(command: String) {
        when (command.type()) {
            "site" -> site()
            "local" -> local()
            "public" -> public()
            "save" -> save(command)
            "load" -> load(command)
            else -> log("Storage:site:local:public:route:save:load\n unknown: $command")
        }
    }
    init{
        createDirectory(site())
        createDirectory(local())
        createDirectory(public())
    }


    fun site(): String {
        return "$site"
    }

    fun local(): String {
        return "${site()}\\local"
    }

    fun public(): String {
        return "${site()}\\public"
    }

    fun save(item: String) {
        val path = "${local()}\\${item.type()}"
        println("save:$path")
        PersistentMap(path).save((item).key(), item)
    }


    fun load(arg: String): PersistentMap {
        return PersistentMap(local() + arg)
    }

    fun createDirectory(path: String): Boolean {
        val dir = File(path)
        println(""+dir.path)
        return if (!dir.exists()) {
            dir.mkdirs()
        } else {
            dir.isDirectory
        }
    }
}

