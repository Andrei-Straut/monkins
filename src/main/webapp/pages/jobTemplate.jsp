

<div>
    <a href="{{job.url}}" target="_blank" class="jobDisplay" ng-class="{true: 'BUILDING', false: ''}[job.associatedJob.isBuilding]">
        <h2 class="jobName">
            {{job.associatedJob.monitorDisplayName}}
        </h2>

        <div id="jobBuildData" class="jobBuildData">
            {{lastStableDetails}}<br/>
            {{lastFailedDetails}}<br/>
            {{testDetails}}
        </div>
    </a>
</div>