// app/page.js
"use client"

import { useState, useRef } from 'react'

export default function Home() {
  const [users, setUsers] = useState({
    admin: 'password',
    user1: '1234',
    guest: 'guest'
  })
  const [allTasks, setAllTasks] = useState({})
  const [allChecked, setAllChecked] = useState({})
  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [isSignUp, setIsSignUp] = useState(false)
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const inputRef = useRef(null)

  const tasks = allTasks[username] || []
  const checked = allChecked[username] || {}

  function handleLogin() {
    if (users[username] && users[username] === password) {
      setIsLoggedIn(true)
    } else {
      alert('Invalid login')
    }
  }

  function handleSignUp() {
    if (!username || !password) {
      alert('Please enter a username and password')
    } else if (users[username]) {
      alert('Username already exists')
    } else {
      setUsers({ ...users, [username]: password })
      setIsLoggedIn(true)
    }
  }

  function handleLogout() {
    setIsLoggedIn(false)
    setUsername('')
    setPassword('')
  }

  function addTask() {
    const value = inputRef.current.value.trim()
    if (value !== '') {
      const updatedTasks = [...tasks, value]
      setAllTasks({ ...allTasks, [username]: updatedTasks })
      inputRef.current.value = ''
    }
  }

  function toggleCheck(task) {
    const updatedChecked = { ...checked, [task]: !checked[task] }
    setAllChecked({ ...allChecked, [username]: updatedChecked })
  }

  function removeTask(task) {
    const updatedTasks = tasks.filter(t => t !== task)
    const updatedChecked = { ...checked }
    delete updatedChecked[task]
    setAllTasks({ ...allTasks, [username]: updatedTasks })
    setAllChecked({ ...allChecked, [username]: updatedChecked })
  }

  function InputField() {
    return (
      <>
        <div>Task: <input id="task" type="text" ref={inputRef} /></div>
        <button className="bg-blue-500 rounded text-white px-4" onClick={addTask}>Add</button>
      </>
    )
  }

  if (!isLoggedIn) {
    return (
      <main className="flex flex-col items-center justify-center min-h-screen p-8">
        <h1 className="text-2xl mb-4">{isSignUp ? 'Sign Up' : 'Login'}</h1>
        <input
          className="border px-2 py-1 mb-2"
          placeholder="Username"
          value={username}
          onChange={e => setUsername(e.target.value)}
        />
        <input
          type="password"
          className="border px-2 py-1 mb-4"
          placeholder="Password"
          value={password}
          onChange={e => setPassword(e.target.value)}
        />
        <button
          className="bg-blue-500 text-white px-4 py-2 rounded mb-2"
          onClick={isSignUp ? handleSignUp : handleLogin}
        >
          {isSignUp ? 'Sign Up' : 'Login'}
        </button>
        <button
          className="text-blue-600 underline text-sm"
          onClick={() => setIsSignUp(!isSignUp)}
        >
          {isSignUp ? 'Already have an account? Login' : "Don't have an account? Sign Up"}
        </button>
      </main>
    )
  }

  return (
    <main className="flex min-h-screen flex-col items-center justify-between p-24">
      <div className="w-full max-w-md flex justify-between items-center mb-4">
        <h1 className="text-2xl">Todo List ({username})</h1>
        <button onClick={handleLogout} className="text-sm text-red-500 border px-2 py-1 rounded">Logout</button>
      </div>
      <InputField />
      {tasks.map(task => (
        <div key={task} className="flex items-center gap-4">
          <span
            onClick={() => toggleCheck(task)}
            className={
              "cursor-pointer" +
              (checked[task] ? " line-through text-gray-500" : "")
            }
          >
            {task}
          </span>
          <button onClick={() => removeTask(task)} className="text-red-500">✕</button>
        </div>
      ))}
    </main>
  )
}

