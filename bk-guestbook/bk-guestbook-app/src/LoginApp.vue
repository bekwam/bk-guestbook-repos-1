<template>
    <div class="container is-max-desktop">
        <h1 class="title">Form Auth Demo</h1>
        <form action="j_security_check" method="POST">
            <div class="field">
                <label class="label">Username</label>
                <div class="control">
                    <input class="input" type="text" name="j_username"/>
                </div>
            </div>
            <div class="field">
                <label class="label">Password</label>
                <div class="control">
                    <input class="input" type="secret" name="j_password"/>
                </div>
            </div>
            <div class="buttons">
                <input class="button is-success" type="submit" value="Login"/>
            </div>
        </form>
    </div>
 </template>
<style lang="scss">
@import "bulma/bulma.sass";
</style>