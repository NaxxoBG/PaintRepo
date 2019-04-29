package exec

import engine.DrController
import view.DrView

object MainGraphr {
  def main(args: Array[String]): Unit = {
    val view = new DrView()
    val controller = new DrController(view)
    controller.exec()
  }
}
