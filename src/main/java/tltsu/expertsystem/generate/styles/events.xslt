<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="events">

        <xsl:for-each select="event">
           static <xsl:value-of select="type"/> _<xsl:value-of select="name"/> = "<xsl:value-of select="value"/>" ;
        </xsl:for-each>

    </xsl:template>

</xsl:stylesheet>


<!--
<?xml version="1.0" encoding="UTF-8" ?>
<events>
    <event name="A">
        <value>/[\s\n]/</value>
        <type>regexp</type>
    </event>
    <event name="S">
        <value> </value>
        <type>String</type>
    </event>
    <event name="N">
        <value>\n</value>
        <type>String</type>
    </event>
</events>
-->