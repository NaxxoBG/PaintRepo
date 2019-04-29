package engine

import view.DrView

class DrController(mainWindow: DrView) {

  def exec(): Unit = {
    mainWindow.exec()
  }
}
