object BresenhamLineAlgorithm{
  sealed abstract class IntList;
  case class Nil() extends IntList;
  case class Coord(x:Int,y:Int) extends IntList
  case class Cons(crd:Coord,tl:IntList) extends IntList;

  def DeltaCalc(val1:Int,val2:Int):Int ={
    val dval = val2 - val1
    dval
  }

  def FindDErr(dx:Int,dy:Int):Double={
    math.abs(dy/dx).toDouble
  }

  def Sign(num:Int):Int={
    if(num>0)
      1
    else if(num<0)
      -1
    else
      0
  }

  def concat(list1: IntList, list2: IntList): IntList = list1 match{
    case Nil() => list2
    case Cons(hd,tl) => Cons(hd,concat(tl,list2))
  }

  def LineRc(list:IntList,x0:Int,y0:Int,x1:Int,y1:Int,err:Double): IntList = //list match
  {
    val dx = DeltaCalc(x0,x1)
    val dy = DeltaCalc(y0,y1)
    val tlist = concat(list,Coord(x0,y0))
    if(dx == 0 && dy ==0){tlist}//Done
    if(dx == 0 || dy == 0){
      LineRc(tlist,x0+Sign(dx),y0+Sign(dy),x1,y1,err)
    } //is vertical or horizontal

    val derr = FindDErr(dx,dy)
    val ploterr = err+derr

    if(ploterr >=0.5)
      LineRc(tlist,x0+Sign(dx),y0+Sign(dy),x1,y1,ploterr-1.0)
    else
      LineRc(tlist,x0+1,y0,x1,y1,ploterr)

  }

  /*def DrawLine(list: IntList,x0:Int,y0:Int,x1:Int,y1:Int)={
    LineRc(list,x0,y0,x1,y1,0.0,y0)
  }*/

}