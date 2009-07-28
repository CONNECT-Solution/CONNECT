package nhinc

class PropertyReader
{
   PropertyReader() {}
   
   def static get(context, propertyname) {
      return context.expand( propertyname )
   }


}