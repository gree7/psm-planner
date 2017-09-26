#!/usr/bin/python
import sys
import re
import math
from collections import OrderedDict

__author__ = 'pah'

actions = OrderedDict()
actions["ValuesMerging"] = "-mv"
actions["ValuesMergingBigOperator"] = "-bv"
actions["VariableDeletion"] = "-dv"
actions["ShrinkSimilarOperators"] = "-mo"
actions["DeadEnds"] = "-de"
actions["ValuesPruning"] = "-uv"
actions["SimpleDependencyReduction"] = "-sd"
actions["HalfCycle"] = "-hc"
actions["OneUsage"] = "-ou"
actions["OneEffectDelete"] = "-oe"
actions["SingleFirstActionMergeWithStart"] = "-sf"
actions["UseApplicableActionInStart"] = "-ai"
actions["ActionStartMerge"] = "-as"
actions["GeneralizedAction"] = "-ga"
#actions["ConstantValue"] = "-cv"


class Record(object):
    isGoalSubsetOfInit = False
    inconsistent = True

    origOperators = 0
    activeOperators = 0
    origVariables = 0
    activeVariables = 0
    origGoals = 0
    activeGoals = 0
    origValues = 0
    activeValues = 0
    computingTime = 0
    planningTime = 0
    reductionTime = 0
    recoveryTime = 0

    def __init__(self, domain, problem, line="-1"):
        self.domain = domain
        self.problem = problem
        self.name = domain + ":" + problem

        self.memory = dict()

        if "-1" == line:
            return

        self.solved = Record.processSolved(line)
        self.unsat = Record.processUnsat(line)
        if self.solved & (not self.unsat):
            line = line.replace('-', '')
            self.fillInStats(line)
        if self.unsat:
            self.mv = "unsat instance"

    def fillInStats(self, line):
        for action in actions.keys():
            self.addReduction(action, Record.retrieveNumber(action, line))

        self.isGoalSubsetOfInit = Record.retrieveGoalSubset(line)

        variables = Record.retrieveNumbers("Variables:", line)
        self.activeVariables = int(variables[0])
        self.origVariables = int(variables[1])

        operators = Record.retrieveNumbers("Operators:", line)
        self.activeOperators = int(operators[0])
        self.origOperators = int(operators[1])

        goals = Record.retrieveNumbers("Goals:", line)
        self.activeGoals = int(goals[0])
        self.origGoals = int(goals[1])

        values = Record.retrieveNumbers("Values:", line)
        self.activeValues = int(values[0])
        self.origValues = int(values[1])

        self.computingTime = Record.retrieveNumber("computing time:", line)
        self.planningTime = Record.retrieveNumber("planning", line)
        self.reductionTime = Record.retrieveNumber("reduction", line)
        self.recoveryTime = Record.retrieveNumber("recovery", line)


    @staticmethod
    def processName(line):
        splitted = line.split(" ", 4)
        name = splitted[2]
        if "]" == name[-1]:
            cut = name.index('[')
            return name[0:cut]
        return name

    @staticmethod
    def processSolved(line):
        return (True, False)["###RESULT### FAILED" in line]

    @classmethod
    def processUnsat(cls, line):
        return (False, True)["unsatisfiable" in line]

    @staticmethod
    def retrieveNumbers(base, line):
        splitted = re.findall(base + " (\d+)/(\d+)", line)
        if len(splitted) < 1:
            splitted = re.findall(base + " (\d+) / (\d+)", line)
        return splitted[0]

    @staticmethod
    def retrieveGoalSubset(line):
        splitted = re.findall("Is goal subset of init: true", line)
        if len(splitted) > 0:
            return True
        return False


    @staticmethod
    def retrieveNumber(base, line):
        splitted = re.findall(base + " (\d+)", line)
        if len(splitted) < 1:
            splitted = re.findall(base + "</td><td>(\d+)", line)

        sum = 0

        for found in splitted:
            sum += int(re.findall("\d+", found)[0])

        return sum



    def getReductionCount(self, action):
        if not (action in self.memory.keys()):
            return 0
        return self.memory[action]

    def addReduction(self, action, count):
        if not (action in self.memory.keys()):
            self.memory[action] = 0
        if self.memory[action] == float('nan'):
            self.memory[action] = 0
        self.memory[action] += count


    def getStrReduction(self):
        return Record.getTimeFormatted(self.reductionTime, self.computingTime)

    def getStrRecovery(self):
        return Record.getTimeFormatted(self.recoveryTime, self.computingTime)

    def getStrPlanning(self):
        return Record.getTimeFormatted(self.planningTime, self.computingTime)

    def getComputingTime(self):
        return Record.formatTime(float(self.computingTime) / 1000.0)

    def getRecoveryTime(self):
        return Record.formatTime(float(self.recoveryTime) / 1000.0)

    def getReductionTime(self):
        return Record.formatTime(float(self.reductionTime) / 1000.0)

    def getPlanningTime(self):
        return Record.formatTime(float(self.planningTime) / 1000.0)

    @staticmethod
    def formatTime(time):
        return float("{0:.2f}".format(time))

    @staticmethod
    def getTimeFormatted(time, computingTime):
        if 0 == computingTime:
            return str(0)
        return str(float("{0:.4f}".format(float(time) / float(computingTime))))

    @staticmethod
    def getActStr(active, orig):
        if 0 == orig:
            return str(0)
        return str(100 * (float("{0:.4f}".format(float(active) / orig))))

    @staticmethod
    def parseDomainAndNameRecord(line):
        parsed = Record.processName(line)
        return parsed.split(":", 1)


class Experiments:
    memory = {}

    def __init__(self, source):
        with open(source) as f:
            for line in f:
                if (len(line) > 3) & ("###" == line[0:3]):

                    problem = Record.parseDomainAndNameRecord(line)
                    if not (problem[0] in self.memory):
                        self.memory[problem[0]] = []
                    self.memory[problem[0]].append(Record(problem[0], problem[1], line))
        return

        # def printAll(self):
        # for rec in self.memory:
        # print(rec.__str__())

    def sort(self, arg):
        for domain in self.memory.values():
            if ("-mv" == arg):
                sorted(domain, key=lambda record: record.getMV())
            if ("-dv" == arg):
                sorted(domain, key=lambda record: record.getDV())
            if ("-mw" == arg):
                sorted(domain, key=lambda record: record.getMW())
            if ("-mo" == arg):
                sorted(domain, key=lambda record: record.getMO())
            if ("-sd" == arg):
                sorted(domain, key=lambda record: record.getSD())
            if ("-de" == arg):
                sorted(domain, key=lambda record: record.getDE())
            if ("-uv" == arg):
                sorted(domain, key=lambda record: record.getUV())
        return


class TexCreator(object):
    begin = "\\documentclass[multi=varwidth]{standalone} \\usepackage{makecell} \\begin{document}  "
    tableBegin = "\\begin{varwidth}  \\begin{tabular}{| r | "  + len(actions.keys()) * " c | " + " c || c | c || c | c || c | c || c | c || c || c | c | c | c | }"
    head = " & "
    for key in actions.keys():
        head += actions[key] + " & "
    head += " \\thead{is goal \\\\ subset of \\\\ init} & \\thead{active \\\\ variables} & \\thead{original \\\\ variables} & \\thead{active \\\\ operators} & \\thead{original  \\\\ operators} & \\thead{active  \\\\ goals} & \\thead{original \\\\  goals} & \\thead{active  \\\\ values} & \\thead{original \\\\  values} & \\thead{solved} & \\thead{processing  \\\\  time [s]} & \\thead{reduction} & \\thead{planning} & \\thead{recovery} \\\\ \\hline \n "
    tableEnd = "\\end{tabular}  \\end{varwidth} \n"
    end = "\\end{document}"
    content = ""

    def __init__(self, experiments):
        self.experiments = experiments

    def produce(self):
        domains = ""
        for domain in self.experiments.memory.values():
            averVar = 0
            averOper = 0
            averGoal = 0
            averVal = 0
            records = 0
            domainStat = Record(domain[0].domain,domain[0].domain)

            self.content += self.tableBegin
            self.content += "\\hline \\thead{" + domain[0].domain + "} " + self.head
            for record in domain:

                # if math.isnan(float(record.getSD())):
                # continue
                #if int(record.getSD()) < 1:
                #    #print "ahoj \t " + str(record.getSD()) + "\n"
                #    continue
                self.content += record.name + " & "
                for key in actions.keys():
                    self.content += str(record.getReductionCount(key)) + " & "

                self.content += str(record.isGoalSubsetOfInit) + " & "
                self.content += TexCreator.getStrVariables(record) + "\% & "
                self.content += str(record.origVariables) + " & "
                self.content += TexCreator.getStrOperators(record) + "\% & "
                self.content += str(record.origOperators) + " & "
                self.content += TexCreator.getStrGoals(record) + "\% & "
                self.content += str(record.origGoals) + " & "
                self.content += TexCreator.getStrValues(record) + "\% & "
                self.content += str(record.origValues) + " & "
                self.content += str(record.solved) + " & "
                self.content += str(record.getComputingTime()) + " & "
                self.content += str(record.getReductionTime()) + " & "
                self.content += str(record.getPlanningTime()) + " & "
                self.content += str(record.getRecoveryTime()) + " \\\\ \\hline \n"

                if record.solved:
                    records += 1
                    averVar += float(TexCreator.getStrVariables(record))
                    averOper += float(TexCreator.getStrOperators(record))
                    averGoal += float(TexCreator.getStrGoals(record))
                    averVal += float(TexCreator.getStrValues(record))
                    for key in actions.keys():
                        domainStat.addReduction(key,record.getReductionCount(key))

            if records > 0:
                averVar = str(float("{0:.2f}".format(float(averVar / records)))) + "\%"
                averOper = str(float("{0:.2f}".format(float(averOper / records)))) + "\%"
                averGoal = str(float("{0:.2f}".format(float(averGoal / records)))) + "\%"
                averVal = str(float("{0:.2f}".format(float(averVal / records)))) + "\%"
            else:
                averVar = "---"
                averOper = "---"
                averGoal = "---"
                averVal = "---"

            domainRecord = "  " + domain[0].domain + " & "
            for key in actions.keys():
                domainRecord += str(domainStat.getReductionCount(key)) + " & "
            domainRecord += "  "
            domainRecord += " & " + str(averVar) + " &"
            domainRecord += " & " + str(averOper) + " & "
            domainRecord += " & " + str(averGoal) + " & "
            domainRecord += " & " + str(averVal) + " & & & & & & \\\\ \\hline \n"
            domains += domainRecord

            self.content += domainRecord
            self.content += self.tableEnd

        self.content += self.tableBegin
        self.content += "\\hline \\thead{domains} " + self.head
        self.content += domains
        self.content += self.tableEnd

    @staticmethod
    def getStrValues(record):
        return Record.getActStr(record.activeValues, record.origValues)

    @staticmethod
    def getStrOperators(record):
        return Record.getActStr(record.activeOperators, record.origOperators)

    @staticmethod
    def getStrVariables(record):
        return Record.getActStr(record.activeVariables, record.origVariables)

    @staticmethod
    def getStrGoals(record):
        return Record.getActStr(record.activeGoals, record.origGoals)


def process(args):
    if (len(args) < 1):
        print "No file at input."
        exit(-1)
    source = args[0]
    experiments = Experiments(source)
    #if (len(args) > 1):
    #    experiments.sort(args[1])

    # experiments.printAll()
    tex = TexCreator(experiments)
    tex.produce()
    print(tex.begin + tex.content + tex.end)

    return


process(sys.argv[1:])