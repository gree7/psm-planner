#!/usr/bin/python

import re
import os
import os.path

assPat = re.compile("^\s*\(\s*=\s*\((\w*)\s*(\w*)\)\s*{(.*)}\)\s*$")
notPat = re.compile("^\s*\(\s*not\s*\(\s*=\s*\((\w*)\s*(\w*)\)\s*{(.*)}\)\s*\)\s*$")

def decompile_problem_file(filename):
    ret = ""
    for line in file(filename):
        line = line.strip("\n")
        mo = assPat.search(line)
        if mo is not None:
            for o in mo.group(3).split(" "):
                o = o.strip()
                if len(o) > 0:
                    ret += " (%s %s %s)\n" % (mo.group(1), mo.group(2), o)
        else:
            mo = notPat.search(line)
            if mo is not None:
                for o in mo.group(3).split(" "):
                    o = o.strip()
                    if len(o) > 0:
                        ret += " (not (%s %s %s))\n" % (mo.group(1), mo.group(2), o)
            elif line.find("{") != -1:
                print "ERROR: "+line
            else:
                ret += line + "\n"
    return ret


def decompile_problems():
    for (path, dirs, files) in os.walk('.'):
        for filename in files:
            if filename.startswith("Problem"):
                filepath = os.path.join(path, filename)
                newproblem = decompile_problem_file(filepath)
                
                backup = filepath+".orig"
                os.rename(filepath, backup)
                f = file(filepath, "w")
                f.write(newproblem)
                f.close()

                #
                #print os.path.join(path, filename)
                print filepath

decompile_problems()

