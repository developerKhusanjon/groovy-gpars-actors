package dataflow

def data = new DataflowVariable()
thread {
    data << computeData()
}
thread {
    processData(data.val)
}
