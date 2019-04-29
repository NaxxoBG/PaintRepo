package au.controller

import au.view.DrView

class DrController(mainWindow: DrView) {

  def exec(): Unit = {
    mainWindow.launch()
  }
}
