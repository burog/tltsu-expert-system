<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="states">
        enum A {
        <xsl:for-each select="state">
            <xsl:value-of select="upper-case(name)"/>
            <xsl:if test="not(position()=last())">, </xsl:if>
        </xsl:for-each>
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