console.log("Employees: " + JSON.stringify(listEmployees));

var chartData = {
  labels: [],
  datasets: []
};

var projectNamesMap = new Map();

for (var i = 0; i < listEmployees.length; i++) {
  var employee = listEmployees[i];
  var employeeName = employee.name;
  var employeeProjects = employee.chartEmployeeProjects;

  chartData.labels.push(employeeName);

  for (var j = 0; j < employeeProjects.length; j++) {
    var project = employeeProjects[j];
    var projectName = project.projectName;
    var employeeBookedMonths = project.employeeBookedMonths || 0;
    var percentage = project.percentage || '';

    if (!projectNamesMap.has(projectName)) {
      projectNamesMap.set(projectName, getRandomColor(projectName));
    }

    var datasetIndexMap = {};

    var datasetIndex = datasetIndexMap[projectName];

    if (datasetIndex === undefined) {
      var dataset = {
        label: projectName,
        data: [],
        backgroundColor: projectNamesMap.get(projectName),
        borderColor: 'rgba(0, 0, 0, 1)',
        borderWidth: 1,
        stack: 'Stack 1',
        percentage: []
      };

      chartData.datasets.push(dataset);

      datasetIndex = chartData.datasets.length - 1;
      datasetIndexMap[projectName] = datasetIndex;
    }

    var employeeIndex = chartData.labels.indexOf(employeeName);
    var previousValue = chartData.datasets[datasetIndex].data[employeeIndex] || 0;
    chartData.datasets[datasetIndex].data[employeeIndex] = previousValue + employeeBookedMonths;
    chartData.datasets[datasetIndex].percentage[employeeIndex] = percentage;
  }
}

var legendLabelsMap = new Map();

for (var i = 0; i < chartData.datasets.length; i++) {
  var dataset = chartData.datasets[i];
  var label = dataset.label;
  var backgroundColor = dataset.backgroundColor;

  if (!legendLabelsMap.has(label)) {
    legendLabelsMap.set(label, {
      text: label,
      fillStyle: backgroundColor,
      strokeStyle: backgroundColor,
      hidden: false
    });
  }
}

var legendLabels = Array.from(legendLabelsMap.values());

var chartOptions = {
  plugins: {
    legend: {
      display: true,
      labels: {
        generateLabels: function(context) {
          var legendLabels = Array.from(legendLabelsMap.values());
          return legendLabels;
        }
      }
    },
    tooltip: {
      callbacks: {
        label: function(context) {
          var label = context.dataset.label || '';
          var months = context.parsed.y || 0;
          var percentage = context.dataset.percentage ? context.dataset.percentage[context.dataIndex] : '';

          if (percentage !== '' && (percentage > 0 && percentage < 100)) {
            return [label + ': ' + months + ' months', 'Percentage: ' + percentage + '%'];
          }

          return label + ': ' + months + ' months';
        }
      }
    }
  }
};

var ctx = document.getElementById('myChart').getContext('2d');
new Chart(ctx, {
  type: 'bar',
  data: chartData,
  options: chartOptions
});

function getRandomColor() {
  var letters = '0123456789ABCDEF';
  var color = '#';
  for (var i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)];
  }
  return color;
}
