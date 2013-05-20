<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="FSM">

@SuppressWarnings("unchecked")
	static &lt;E extends Object&gt; FSM&lt;A, E&gt; createA() {
		return new FSM&lt;A, E&gt;(

  <xsl:for-each select="group-transition">
    new State&lt;A, E&gt;
    (A.<xsl:value-of select="@source"/>)
     {
         public void handleEvent()
         {
               <xsl:for-each select="transition">
               <xsl:if test="position() != 1"> else </xsl:if>
                 if (e().equals( _<xsl:value-of select="event"/> ))
                 {
                    <xsl:value-of select="code"/>
                    next(A.<xsl:value-of select="dest-state"/>);
                 }
               </xsl:for-each>
         }
     }
            <xsl:if test="not(position()=last())">, </xsl:if>
  </xsl:for-each>


        );
    }
    </xsl:template>

</xsl:stylesheet>


<!--
<?xml version="1.0" encoding="ISO-8859-1"?>

    <states>
        <state><name>after</name></state>
        <state><name>inside</name></state>
        <state><name>before</name></state>
    </states>
-->