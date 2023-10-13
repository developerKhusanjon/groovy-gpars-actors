package dataflow

@Grab("org.codehaus.gpars:gpars:1.2.1")

import groovyx.gpars.dataflow.DataflowVariable

def data = new DataflowVariable()
thread {
    data << computeData()
}
thread {
    processData(data.val)
}
