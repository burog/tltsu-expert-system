<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="states">
        enum A {
        <xsl:for-each select="state">
            <xsl:value-of select="."/>
            <xsl:if test="not(position()=last())">, </xsl:if>
        </xsl:for-each>
        }
    </xsl:template>

</xsl:stylesheet>

<!--
<?xml version="1.0" encoding="ISO-8859-1"?>
    <states>
        <state>after</state>
        <state>inside</state>
        <state>before</state>
    </states>
-->