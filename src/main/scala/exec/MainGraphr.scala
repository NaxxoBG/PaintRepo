package au.exec

import au.controller.DrController
import au.view.DrView


object MainGraphr {
  def main(args: Array[String]): Unit = {
    val view = new DrView()
    val controller = new DrController(view)
    controller.exec()
  }
}
