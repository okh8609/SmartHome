# -*- coding: utf-8 -*-

import xml.etree.ElementTree as et
root = et.parse('android_XML_parse_to_object_data.xml').getroot()

ns = {'android': 'http://schemas.android.com/apk/res/android'}

items = root.findall(".//*[@android:id]", namespaces=ns)

for item in items:
    var_type = item.tag
    var_name = item.attrib['{%s}id' % ns['android']][5:]
    print("{0} {1} = (({2}) root.findViewById(R.id.{3}));"
          .format(var_type, var_name, var_type, var_name))

print("\n\n// ********** ********** **********\n\n")

for item in items:
    var_type = item.tag
    var_name = item.attrib['{%s}id' % ns['android']][5:]
    print("{0} {1};".format(var_type, var_name))

print("\n\n// ********** ********** **********\n\n")

for item in items:
    var_type = item.tag
    var_name = item.attrib['{%s}id' % ns['android']][5:]
    print("{0} = (({1}) root.findViewById(R.id.{2}));"
          .format(var_name, var_type, var_name))


# print attributes for an element:
# for name, value in item.attrib.items():
#     print('{0}="{1}"'.format(name, value))
