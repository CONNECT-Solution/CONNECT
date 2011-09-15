def inputXmlPath = args[0]
def outputXmlPath = args[1]

def xmlParser = new XmlParser()
def document = xmlParser.parse(new File(inputXmlPath))

def docQueryTestCaseElement = document.find {
    it.name()=='testcase' && it.'@name' == 'Document Query'
}  

document.remove(docQueryTestCaseElement)


def writer = new PrintWriter(new FileWriter(new File(outputXmlPath)))
new XmlNodePrinter(writer).print(document)
