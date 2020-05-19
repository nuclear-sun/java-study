package com.sun.classloader

import java.io._

object ClassManipulator {

  def saveClassFile(obj: AnyRef): Unit = {

    val classLoader = obj.getClass.getClassLoader()
    val className = obj.getClass.getName
    val classFile = className.replace(".", "/") + ".class"
    val stream = classLoader.getResourceAsStream(classFile)

    val outputFileName = obj.getClass.getSimpleName + ".class"
    val fileStream = new FileOutputStream(outputFileName)

    val buffer = new Array[Byte](1024)

    var n = 1
    while (n > 0) {
      n = stream.read(buffer)
      if(n > 0) {
        fileStream.write(buffer, 0, n)
      }
    }
    fileStream.flush()
    fileStream.close()
    stream.close()
  }
}

object FileSerializer {
  @throws[IOException]
  def writeObjectToFile(`object`: Any, file: String): Unit = {
    val fileOutputStream = new FileOutputStream(file)
    val objectOutputStream = new ObjectOutputStream(fileOutputStream)
    objectOutputStream.writeObject(`object`)
    objectOutputStream.close()
  }

  @throws[IOException]
  @throws[ClassNotFoundException]
  def readObjectFromFile(file: String): Any = {
    val inputStream = new FileInputStream(file)
    val objectInputStream = new ObjectInputStream(inputStream)
    val `object` = objectInputStream.readObject
    objectInputStream.close()
    `object`
  }
}
